package ru.otus.kudaiberdieva.homework3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    List<Task> tasks;

    @BeforeEach
    void init() {
        tasks = new ArrayList<>();
        tasks.add(new Task().setId(1).setTitle("Task1").setStatus(Status.OPENED));
        tasks.add(new Task().setId(2).setTitle("Task2").setStatus(Status.WORKING));
        tasks.add(new Task().setId(3).setTitle("Task3").setStatus(Status.CLOSED));
        tasks.add(new Task().setId(4).setStatus(Status.OPENED));
        tasks.add(new Task().setId(5).setTitle("Task5").setStatus(Status.CLOSED));
        tasks.add(new Task().setId(6).setTitle("Task6").setStatus(Status.CLOSED));
        tasks.add(new Task().setId(7).setTitle("Task7").setStatus(Status.OPENED));
        tasks.add(new Task().setId(8).setTitle("Task8").setStatus(Status.WORKING));
        tasks.add(new Task().setId(9).setStatus(Status.WORKING));
        tasks.add(new Task().setId(10).setTitle("Task10").setStatus(Status.CLOSED));
    }

    @Test
    void test_getTaskByStatus(){
        List<Task> tasksOpened = tasks.stream().filter(t -> (t.getStatus() != null)).filter(t -> (t.getStatus().equals(Status.OPENED))).toList();
        assertEquals(3, tasksOpened.size());

        List<Task> tasksWorking = tasks.stream().filter(t -> (t.getStatus() != null)).filter(t -> (t.getStatus().equals(Status.WORKING))).toList();
        assertEquals(3, tasksWorking.size());

        List<Task> tasksClosed = tasks.stream().filter(t -> (t.getStatus() != null)).filter(t -> (t.getStatus().equals(Status.CLOSED))).toList();
        assertEquals(4, tasksClosed.size());
    }

    @Test
    void test_taskGetById(){
        Optional<Task> task1 = tasks.stream().filter(t -> t.getId() == 5).findAny();
        assertTrue(task1.isPresent());

        Optional<Task> task2 = tasks.stream().filter(t -> t.getId() == 11).findAny();
        assertFalse(task2.isPresent());
    }
    @Test
    void test_tasksSortedByStatus(){
        TaskManager taskManager = new TaskManager();
        List<Task> sortedTasks = taskManager.getTasksSortedByStatus(tasks);

        assertFalse(sortedTasks.isEmpty());
        assertTrue(sortedTasks.stream().allMatch(task -> task.getStatus() != null));
        for (int i = 0; i < sortedTasks.size() - 1; i++) {
            Task currentTask = sortedTasks.get(i);
            Task nextTask = sortedTasks.get(i + 1);
            assertTrue(currentTask.getStatus().compareTo(nextTask.getStatus()) <= 0);
        }
    }
    @Test
    void test_countTaskByStatus(){
        long taskOpened = tasks.stream().filter(t -> t.getStatus() != null).filter(t -> t.getStatus() == Status.OPENED).count();
        assertEquals(3, taskOpened);

        long taskWorking = tasks.stream().filter(t -> t.getStatus() != null).filter(t -> t.getStatus() == Status.WORKING).count();
        assertEquals(3, taskWorking);

        long taskClosed = tasks.stream().filter(t -> t.getStatus() != null).filter(t -> t.getStatus() == Status.CLOSED).count();
        assertEquals(4, taskClosed);
    }

}
