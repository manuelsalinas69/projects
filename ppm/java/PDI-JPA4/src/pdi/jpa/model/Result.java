package pdi.jpa.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the "RESULT" database table.
 * 
 */
@Entity
@Table(name="RESULT")
@NamedQuery(name="Result.findAll", query="SELECT r FROM Result r")
public class Result implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private Date endTime;
	private byte[] outputImg;
	private Long runnerId;
	private Long slide;
	private Date startTime;
	private Long winSize;
	private Image image;
	private String params;

	public Result() {
	}


	@Id
	//@SequenceGenerator(name="RESULT_ID_GENERATOR", sequenceName="RESULT_SEQ", allocationSize=1)
	//@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="RESULT_ID_GENERATOR")
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}


	@Column(name="END_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}


	@Lob
	@Column(name="OUTPUT_IMG")
	public byte[] getOutputImg() {
		return this.outputImg;
	}

	public void setOutputImg(byte[] outputImg) {
		this.outputImg = outputImg;
	}


	@Column(name="RUNNER_ID")
	public Long getRunnerId() {
		return this.runnerId;
	}

	public void setRunnerId(Long runnerId) {
		this.runnerId = runnerId;
	}


	public Long getSlide() {
		return this.slide;
	}

	public void setSlide(Long slide) {
		this.slide = slide;
	}


	@Column(name="START_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}


	@Column(name="WIN_SIZE")
	public Long getWinSize() {
		return this.winSize;
	}

	public void setWinSize(Long winSize) {
		this.winSize = winSize;
	}


	
	@Column(name="PARAMS")
	public String getParams() {
		return params;
	}


	public void setParams(String params) {
		this.params = params;
	}


	//bi-directional many-to-one association to Image
	@ManyToOne
	@JoinColumn(name="INPUT_IMG_ID")
	public Image getImage() {
		return this.image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

}