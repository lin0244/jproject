package com.enbiso.proj.jproject.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Task.
 */
@Entity
@Table(name = "app_task_tab")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Task implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne
    private Iteration iteration;

    @ManyToOne
    private Person assignee;

    @ManyToOne
    private Person owner;

    @ManyToOne
    private TaskType type;

    @ManyToOne
    private TaskStatus status;

    @ManyToOne
    private TaskPriority priority;

    @ManyToOne
    private TaskImportance importance;

    @OneToMany(mappedBy = "parentTask")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Task> subTasks = new HashSet<>();

    @ManyToOne
    private Task parentTask;

    @OneToMany(mappedBy = "task")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TaskComment> comments = new HashSet<>();

    @OneToMany(mappedBy = "task")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TaskLog> logs = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Iteration getIteration() {
        return iteration;
    }

    public void setIteration(Iteration iteration) {
        this.iteration = iteration;
    }

    public Person getAssignee() {
        return assignee;
    }

    public void setAssignee(Person person) {
        this.assignee = person;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person person) {
        this.owner = person;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType taskType) {
        this.type = taskType;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus taskStatus) {
        this.status = taskStatus;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority taskPriority) {
        this.priority = taskPriority;
    }

    public TaskImportance getImportance() {
        return importance;
    }

    public void setImportance(TaskImportance taskImportance) {
        this.importance = taskImportance;
    }

    public Set<Task> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(Set<Task> tasks) {
        this.subTasks = tasks;
    }

    public Task getParentTask() {
        return parentTask;
    }

    public void setParentTask(Task task) {
        this.parentTask = task;
    }

    public Set<TaskComment> getComments() {
        return comments;
    }

    public void setComments(Set<TaskComment> taskComments) {
        this.comments = taskComments;
    }

    public Set<TaskLog> getLogs() {
        return logs;
    }

    public void setLogs(Set<TaskLog> taskLogs) {
        this.logs = taskLogs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Task task = (Task) o;

        if ( ! Objects.equals(id, task.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Task{" +
            "id=" + id +
            ", title='" + title + "'" +
            '}';
    }
}
