package ttl.larku.controllers.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.ArrayList;
import java.util.List;

public class RestResultGeneric<T> {

    public enum Status {
        Ok,
        Error
    }

    private Status status = Status.Ok;

    private List<String> errors = new ArrayList<>();

    @JsonInclude(Include.NON_NULL)
    private T entity;

    public RestResultGeneric() {
    }

    public RestResultGeneric(String message) {
        errors.add(message);
        //We assume that creating with a message means errors
        this.status = Status.Error;
    }

    public RestResultGeneric(Status status, String message) {
        this.status = status;
        errors.add(message);
    }


    public RestResultGeneric(Status status, List<String> errors) {
        this.status = status;
        this.errors = errors;
    }

    public RestResultGeneric(T entity) {
        this.entity = entity;
    }

    public RestResultGeneric(Status status, T entity) {
        this.status = status;
        this.entity = entity;
    }


    /**
     * "Builder type Api
     *
     * @param entity
     * @return
     */
    public RestResultGeneric<T> entity(T entity) {
        this.entity = entity;
        return this;
    }

    public RestResultGeneric<T> status(Status status) {
        this.status = status;
        return this;
    }

    @JsonIgnore
    public String getErrorMessage() {
        return errors.get(0);
    }

    @JsonIgnore
    public void setErrorMessage(String errorMessage) {
        errors.add(0, errorMessage);
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public T getEntity() {
        return entity;
    }

	/*
	public <T> T getEntity(Class<T> clazz) {
		Map map = (Map)entity;
		
		return clazz.cast(entity);
	}
	*/

    public void setEntity(T entity) {
        this.entity = entity;
    }

    @Override
    public String toString() {
        return "RestResult [status=" + status + ", errors=" + errors + ", entity=" + entity + "]";
    }
}
