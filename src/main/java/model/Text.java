package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the text database table.
 * 
 */
@Entity
@Table(name="Text")
@NamedQuery(name="Text.findAll", query="SELECT t FROM Text t")
public class Text implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private long id;

	private String text;

	public Text() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

}