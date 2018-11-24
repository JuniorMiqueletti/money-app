package com.juniormiqueletti.moneyapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.juniormiqueletti.moneyapp.repository.listener.ReleaseAttachmentListener;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@EntityListeners(ReleaseAttachmentListener.class)
@Entity
@Table(name = "releases")
public class Release {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Size(min = 5, max = 50)
	private String description;

	@Column(name = "due_date")
	@NotNull
	private LocalDate dueDate;

	@Column(name = "pay_date")
	private LocalDate payDate;

	@NotNull
	private BigDecimal value;
	
	private String observation;

	@Enumerated(EnumType.STRING)
	@NotNull
	private ReleaseType type;

	@ManyToOne
	@JoinColumn(name = "id_category")
	@NotNull
	private Category category;

	@JsonIgnoreProperties("contacts")
	@ManyToOne
	@JoinColumn(name = "id_person")
	@NotNull
	private Person person;

	@JsonIgnore
	public Boolean isRecipe() {
	    return this.type.RECIPE.equals(this.type);
    }

    private String attachment;

	@Transient
	private String attachedmentUrl;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public LocalDate getPayDate() {
		return payDate;
	}

	public void setPayDate(LocalDate payDate) {
		this.payDate = payDate;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String obervation) {
		this.observation = obervation;
	}

	public ReleaseType getType() {
		return type;
	}

	public void setType(ReleaseType type) {
		this.type = type;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getAttachedmentUrl() {
        return attachedmentUrl;
    }

    public void setAttachedmentUrl(String attachedmentUrl) {
        this.attachedmentUrl = attachedmentUrl;
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Release other = (Release) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
