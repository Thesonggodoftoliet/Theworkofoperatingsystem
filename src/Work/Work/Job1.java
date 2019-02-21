/**
 * 
 */
package Work;

import java.util.Date;

/**
 * @author 李煜峰
 *
 * date:2018年10月8日 time:下午2:18:08
 */
public class Job1 {

	private int num;
	private Date entertime;
	private double runtime_es;
	private Date begintime;

	public Date getBeginagain() {
		return beginagain;
	}

	public void setBeginagain(Date beginagain) {
		this.beginagain = beginagain;
	}

	public double getLefttime() {
		return lefttime;
	}

	public void setLefttime(double lefttime) {
		this.lefttime = lefttime;
	}

	private Date beginagain;
	private Date endtime;
	private double runtime;
	private double lefttime;
	private double runtime_right;
	private int tag;//是否加入输入井
	private double response_ratio;

	
	/**
	 * @return the tag
	 */
	public int getTag() {
		return tag;
	}

	/**
	 * @param tag the tag to set
	 */
	public void setTag(int tag) {
		this.tag = tag;
	}

	/**
	 * @return the response_ratio
	 */
	public double getResponse_ratio() {
		return response_ratio;
	}

	/**
	 * @param response_ratio the response_ratio to set
	 */
	public void setResponse_ratio(double response_ratio) {
		this.response_ratio = response_ratio;
	}

	/**
	 * @return the entertime
	 */
	public Date getEntertime() {
		return entertime;
	}

	/**
	 * @param entertime the entertime to set
	 */
	public void setEntertime(Date entertime) {
		this.entertime = entertime;
	}

	/**
	 * @return the runtime_es
	 */
	public double getRuntime_es() {
		return runtime_es;
	}

	/**
	 * @param runtime_es the runtime_es to set
	 */
	public void setRuntime_es(double runtime_es) {
		this.runtime_es = runtime_es;
	}

	/**
	 * @return the begintime
	 */
	public Date getBegintime() {
		return begintime;
	}

	/**
	 * @param begintime the begintime to set
	 */
	public void setBegintime(Date begintime) {
		this.begintime = begintime;
	}

	/**
	 * @return the endtime
	 */
	public Date getEndtime() {
		return endtime;
	}

	/**
	 * @param endtime the endtime to set
	 */
	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}

	/**
	 * @return the runtime
	 */
	public double getRuntime() {
		return runtime;
	}

	/**
	 * @param runtime the runtime to set
	 */
	public void setRuntime(double runtime) {
		this.runtime = runtime;
	}

	/**
	 * @return the runtime_right
	 */
	public double getRuntime_right() {
		return runtime_right;
	}

	/**
	 * @param runtime_right the runtime_right to set
	 */
	public void setRuntime_right(double runtime_right) {
		this.runtime_right = runtime_right;
	}

	/**
	 * @param entertime
	 * @param runtime_es
	 */
	public Job1(Date entertime, double runtime_es, int tag,int num) {
		super();
		this.entertime = entertime;
		this.runtime_es = runtime_es;
		this.lefttime = runtime_es;
		this.tag = tag;
		this.num = num;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	/**
	 * 
	 */
	public Job1() {
		// TODO Auto-generated constructor stub
	}

}
