/**
 * 
 */
package Work;

/**
 * @author 李煜峰
 *
 * date:2018年10月27日 time:上午10:42:43
 */
public class PCB {
	
	private String name;
	private int id;
	private int priority;
	private int status;//-1 waiting 0 ready 1 running
	private String LeftInfo;//保留信息
	private int resourse; //资源信息
	private int parent;//父进程ID
	private int time;//时间

	/**
	 * @return the parent
	 */
	public int getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(int parent) {
		this.parent = parent;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the leftInfo
	 */
	public String getLeftInfo() {
		return LeftInfo;
	}

	/**
	 * @param leftInfo the leftInfo to set
	 */
	public void setLeftInfo(String leftInfo) {
		LeftInfo = leftInfo;
	}

	/**
	 * @return the resourse
	 */
	public int getResourse() {
		return resourse;
	}

	/**
	 * @param resourse the resourse to set
	 */
	public void setResourse(int resourse) {
		this.resourse = resourse;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * 
	 */
	public PCB() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the time
	 */
	public int getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(int time) {
		this.time = time;
	}


}
