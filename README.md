  Домашнее задание:
    - Реализовать класс DbMigrator - он должен при старте создавать все необходимые таблицы из файла init.sql
     Доработать AbstractRepository
     - Доделать findById(id), findAll(), update(), deleteById(id), deleteAll()
     - Сделать возможность указывать имя столбца таблицы для конкретного поля (например, поле accountType маппить на столбец с именем account_type)
    - Добавить проверки, если по какой-то причине невозможно проинициализировать репозиторий, необходимо бросать исключение, чтобы
     программа завершила свою работу (в исключении надо объяснить что сломалось)
     - Работу с полями объектов выполнять только через геттеры/сеттеры
