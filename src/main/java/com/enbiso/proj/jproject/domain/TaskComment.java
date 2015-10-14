package com.enbiso.proj.jproject.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.enbiso.proj.jproject.domain.util.CustomDateTimeDeserializer;
import com.enbiso.proj.jproject.domain.util.CustomDateTimeSerializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A TaskComment.
 */
@Entity
@Table(name = "task_comment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TaskComment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @NotNull        
    @Column(name = "content", nullable = false)
    private String content;

    @NotNull        
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "posted_on", nullable = false)
    private DateTime postedOn;

    @ManyToOne
    private Person commenter;

    @ManyToOne
    private Task task;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public DateTime getPostedOn() {
        return postedOn;
    }

    public void setPostedOn(DateTime postedOn) {
        this.postedOn = postedOn;
    }

    public Person getCommenter() {
        return commenter;
    }

    public void setCommenter(Person person) {
        this.commenter = person;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TaskComment taskComment = (TaskComment) o;

        if ( ! Objects.equals(id, taskComment.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TaskComment{" +
            "id=" + id +
            ", content='" + content + "'" +
            ", postedOn='" + postedOn + "'" +
            '}';
    }
}
