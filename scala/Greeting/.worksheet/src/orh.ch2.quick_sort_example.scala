package orh.ch2

object quick_sort_example {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(245); 

  def qSort(a: List[Int]): List[Int] = {
  	println(a)
    if (a.length < 2) a
    else
      qSort(a.filter(a.head < _)) ++
        a.filter(a.head == _) ++
        qSort(a.filter(a.head > _))
  };System.out.println("""qSort: (a: List[Int])List[Int]""");$skip(42); val res$0 = 

  qSort(List(7, 3, 5, 8, 20, 22, 5, 9));System.out.println("""res0: List[Int] = """ + $show(res$0))}
}
