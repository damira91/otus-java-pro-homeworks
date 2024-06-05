package ru.otus.kudaiberdieva.homework07.repositories;

import ru.otus.kudaiberdieva.homework07.annotations.RepositoryColumn;
import ru.otus.kudaiberdieva.homework07.annotations.RepositoryField;
import ru.otus.kudaiberdieva.homework07.annotations.RepositoryIdField;
import ru.otus.kudaiberdieva.homework07.annotations.RepositoryTable;
import ru.otus.kudaiberdieva.homework07.database.DataSource;
import ru.otus.kudaiberdieva.homework07.exceptions.EntityException;
import ru.otus.kudaiberdieva.homework07.exceptions.RepositoryException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

public class AbstractRepository<T> {
    private DataSource dataSource;
    private Map<String, Method> fieldGetterCash = new HashMap<>();
    private Map<String, Method> fieldSetterCash = new HashMap<>();
    private PreparedStatement psCreate;
    private PreparedStatement psFindAll;
    private PreparedStatement psFindById;
    private PreparedStatement psUpdate;
    private PreparedStatement psDeleteById;
    private PreparedStatement psDeleteAll;
    private List<Field> allFields;
    private List<Field> fieldsWithNoId;
    private Field idField;
    private Class<T> cls;
    String tableName;


    public AbstractRepository(DataSource dataSource, Class<T> cls) {
        this.dataSource = dataSource;
        this.cls = cls;
        this.tableName = cls.getAnnotation(RepositoryTable.class).title();
        prepare(cls);
    }

    private void prepare(Class<T> cls) {
        fieldsWithNoId = Arrays.stream(cls.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(RepositoryField.class))
                .filter(f -> !f.isAnnotationPresent(RepositoryIdField.class))
                .collect(Collectors.toList());
        if (fieldsWithNoId.isEmpty()) {
            throw new RepositoryException("Entity has only identification field");
        }
        allFields = Arrays.stream(cls.getDeclaredFields()).collect(Collectors.toList());
        idField = Arrays.stream(cls.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(RepositoryIdField.class))
                .findFirst().orElseThrow(() -> new RepositoryException("Entity has no necessary identification"));
        fieldGetterCash = allFields.stream().collect(Collectors.toMap(Field::getName, (field) -> {
            String fieldName = field.getName();
            try {
                return cls.getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return null;
        }));
        if (fieldGetterCash.size() != allFields.size()) {
            throw new RepositoryException("The quantity of entity's fields and getter methods are not equal");
        }
        fieldSetterCash = allFields.stream().collect(Collectors.toMap(Field::getName, (field) -> {
            String fieldName = field.getName();
            try {
                return cls.getMethod("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), field.getType());
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return null;
        }));

        if (fieldSetterCash.size() != allFields.size()) {
            throw new RepositoryException("The quantity of entity's fields and setter methods are not equal");
        }
        prepareStatementCreate();
        prepareStatementFindById();
        prepareStatementFindAll();
        prepareStatementUpdate();
        prepareStatementDeleteById();
        prepareStatementDeleteAll();
    }

    public void create(T entity) {
        try {
            for (int i = 0; i < fieldsWithNoId.size(); i++) {
                Field field = fieldsWithNoId.get(i);
                psCreate.setObject(i + 1, fieldGetterCash.get(field.getName()).invoke(entity));
            }
            psCreate.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }
    }

    public void update(T entity) {
        try {
            if (psUpdate == null) {
                prepareStatementUpdate();
            }

            int index = 1;
            for (Field field : fieldsWithNoId) {
                Method getter = fieldGetterCash.get(field.getName());
                Object value = getter.invoke(entity);
                psUpdate.setObject(index++, value);
            }

            Method idGetter = fieldGetterCash.get(idField.getName());
            Object idValue = idGetter.invoke(entity);
            psUpdate.setObject(index, idValue);

            psUpdate.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Error updating entity: " + e.getMessage(), e);
        }
    }

    public void deleteById(Long id) {
        try {
            psDeleteById.setLong(1, id);
            psDeleteById.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    public void deleteAll() {
        try {
            psDeleteAll.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<T> findAll() {
        try {
            List<T> out = new ArrayList<>();
            ResultSet resultSet = psFindAll.executeQuery();

            while (resultSet.next()) {
                T newObject = cls.getConstructor().newInstance();
                allFields.forEach(field -> {
                    try {
                        Method method = fieldSetterCash.get(field.getName());
                        method.invoke(newObject, resultSet.getObject(field.getName(), field.getType()));
                    } catch (IllegalAccessException | InvocationTargetException | SQLException e) {
                        e.printStackTrace();
                    }
                });
                out.add(newObject);
            }
            return out;
        } catch (SQLException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    public T findById(Long id) {
        try {
            psFindById.setLong(1, id);
            ResultSet resultSet = psFindById.executeQuery();
            T newObject = cls.getConstructor().newInstance();

            if (resultSet.next()) {
                allFields.forEach(field -> {
                    try {
                        Method method = fieldSetterCash.get(field.getName());
                        method.invoke(newObject, resultSet.getObject(field.getName(), field.getType()));
                    } catch (IllegalAccessException | InvocationTargetException | SQLException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                throw new EntityException(String.format("Entity not found %s", id));
            }
            return newObject;
        } catch (SQLException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    private String getColumnName(Field field) {
        RepositoryColumn columnAnnotation = field.getAnnotation(RepositoryColumn.class);

        if (columnAnnotation == null) {
            throw new RepositoryException("No RepositoryColumn annotation found on field " + field.getName());
        }

        String columnName = columnAnnotation.name();
        return columnName.isEmpty() ? field.getName() : columnName;
    }

    private String getIdFieldName() {
        return Arrays.stream(cls.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(RepositoryIdField.class))
                .findFirst()
                .map(this::getColumnName)
                .orElseThrow(() -> new RuntimeException("No ID field found"));
    }

    private String getTableName() {
        RepositoryTable annotation = cls.getAnnotation(RepositoryTable.class);
        if (annotation == null) {
            throw new RepositoryException("No RepositoryTable annotation found on class " + cls.getName());
        }
        return annotation.title();
    }

    private void prepareStatementCreate() {
        StringBuilder query = new StringBuilder("insert into ");
        query.append(tableName).append(" (");

        for (Field f : fieldsWithNoId) {
            query.append(f.getName()).append(", ");
        }
        query.setLength(query.length() - 2);
        query.append(") values (");

        for (Field f : fieldsWithNoId) {
            query.append("?, ");
        }

        query.setLength(query.length() - 2);
        query.append(");");
        try {
            psCreate = dataSource.getConnection().prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException e) {
            throw new RepositoryException("Invalid prepareStatement. Error: " + e.getMessage());
        }
    }

    private void prepareStatementUpdate() {
        StringBuilder query = new StringBuilder("UPDATE ");
        query.append(getTableName()).append(" SET ");

        for (Field f : fieldsWithNoId) {
            query.append(getColumnName(f)).append(" = ?, ");
        }

        query.setLength(query.length() - 2); // Remove the last comma and space

        query.append(" WHERE ").append(getColumnName(idField)).append(" = ?");

        try {
            System.out.println("Prepared SQL Update Query: " + query.toString()); // Debug output
            psUpdate = dataSource.getConnection().prepareStatement(query.toString());
        } catch (SQLException e) {
            throw new RuntimeException("Error preparing update statement: " + e.getMessage(), e);
        }
    }

    private void prepareStatementFindAll() {
        try {
            psFindAll = dataSource.getConnection().prepareStatement(String.format("SELECT * FROM %s", tableName));
        } catch (SQLException e) {
            throw new RepositoryException("Invalid prepareStatement to findAll. Error: " + e.getMessage());
        }
    }

    private void prepareStatementFindById() {
        try {
            psFindById = dataSource.getConnection().prepareStatement(String.format("SELECT * FROM %s where %s = ?", tableName, idField.getName()));
        } catch (SQLException e) {
            throw new RepositoryException("Invalid prepareStatement to findById  . Error: " + e.getMessage());
        }
    }

    private void prepareStatementDeleteAll() {
        try {
            psDeleteAll = dataSource.getConnection().prepareStatement(String.format("DELETE FROM %s", tableName));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void prepareStatementDeleteById() {
        try {
            psDeleteById = dataSource.getConnection().prepareStatement(String.format("DELETE FROM %s where %s = ?", tableName, idField.getName()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
