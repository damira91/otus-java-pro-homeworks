package ru.otus.kudaiberdieva.homework3;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class TaskManager {

    public List<Task> getTasksSortedByStatus(List<Task> tasks) {
        return tasks.stream()

                .sorted(Comparator.comparing(task -> {
                    if (task.getStatus() == null) {
                        return Integer.MAX_VALUE;
                    }
                    return task.getStatus().ordinal();
                }))
                .collect(Collectors.toList());
    }
}

