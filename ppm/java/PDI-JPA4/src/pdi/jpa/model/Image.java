package pdi.jpa.model;

import java.io.Serializable;
import javax.persistence.*;
import java.lang.Long;
import java.util.List;


/**
 * The persistent class for the IMAGE database table.
 * 
 */
@Entity
@NamedQuery(name="Image.findAll", query="SELECT i FROM Image i")
public class Image implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private byte[] content;
	private Long height;
	private String mimeType;
	private String name;
	private Long width;
	private List<Result> results;

	public Image() {
	}


	@Id
	//@SequenceGenerator(name="IMAGE_ID_GENERATOR", sequenceName="IMAGE_SEQ", allocationSize=1)
	//@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMAGE_ID_GENERATOR")
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}


	@Lob
	public byte[] getContent() {
		return this.content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}


	public Long getHeight() {
		return this.height;
	}

	public void setHeight(Long height) {
		this.height = height;
	}


	@Column(name="MIME_TYPE")
	public String getMimeType() {
		return this.mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}


	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public Long getWidth() {
		return this.width;
	}

	public void setWidth(Long width) {
		this.width = width;
	}


	//bi-directional many-to-one association to Result
	@OneToMany(mappedBy="image")
	public List<Result> getResults() {
		return this.results;
	}

	public void setResults(List<Result> results) {
		this.results = results;
	}

	public Result addResult(Result result) {
		getResults().add(result);
		result.setImage(this);

		return result;
	}

	public Result removeResult(Result result) {
		getResults().remove(result);
		result.setImage(null);

		return result;
	}

}