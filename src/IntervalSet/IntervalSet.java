package IntervalSet;

import java.util.List;
import java.util.Set;

import IntervalSet.MyExceptions.*;

/**
 * 一种mutable的数据类型。
 * 描述了一组在时间轴上分布的“时间段”，每个时间段附着一个特定的标签，每个标签均对应了一个或多个时间段。
 * 注：由于题设中的IntervalSet显然可以看成是一种每个标签只对应了一个时间段的特殊MultiInterval，则决定直接将题目中原定的两种ADT合二为一。
 * 
 * @param <L> 标签的类型, 必须immutable
 */
public interface IntervalSet<L> {
	/**
	 * 创建一个空的IntervalSet。
	 * 
	 * @param <L> 标签的类型, 必须是不可变的
	 * @return 一个新的空IntervalSet
	 */
	public static <L> IntervalSet<L> empty(){
		return new CommonIntervalSet<L>();
	}

	/**
	 * 在当前IntervalSet对象中插入新的时间段和标签，必须满足start<end
	 * 会自动合并同一标签中首尾相接或是部分重叠的时间段，
	 * 例："A"=[[0,20],[20,30]]->"A"=[[0,30]]
	 * "A"=[[0,20],[10,30]]->"A"=[[0,30]]
	 * 
	 * @param start 时间段的开始时间
	 * @param end 时间段的结束时间
	 * @throws IllegalArgumentxception 如果start>=end
	 * @param label 时间段对应的标签
	 */
	public void insert(long start, long end, L label) throws Exception;

	/**
	 * 获得当前IntervalSet对象中的标签集合。
	 * 
	 * @return 当前IntervalSet对象中的标签集合
	 */
	public Set<L> labels();

	/**
	 * 从当前IntervalSet对象中移除某个标签所关联的所有时间段。
	 * 
	 * @param label 要移除时间段的标签
	 * @return 如果对应标签原来在当前对象中并且已经被移除，返回true，否则返回false
	 */
	public boolean remove(L label);

	/**
	 * 从当前对象中获取与某个标签所关联的所有时间段，返回结果表现为TimePeriod类的一个List，下标按开始时间从小到大的次序排列。
	 * 
	 * @param label 要获取时间段的标签
	 * @throws LabelNotFoundException 如果标签不存在于对象中
	 * @return 与输入的标签所关联的所有时间段的List
	 */
	public List<Interval> intervals(L label) throws LabelNotFoundException;
}
