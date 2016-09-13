package orh.ch2

object quick_sort_example {

  def qSort(a: List[Int]): List[Int] = {
  	println(a)
    if (a.length < 2) a
    else
      qSort(a.filter(a.head < _)) ++
        a.filter(a.head == _) ++
        qSort(a.filter(a.head > _))
  }                                               //> qSort: (a: List[Int])List[Int]

  qSort(List(7, 3, 5, 8, 20, 22, 5, 9))           //> List(7, 3, 5, 8, 20, 22, 5, 9)
                                                  //| List(8, 20, 22, 9)
                                                  //| List(20, 22, 9)
                                                  //| List(22)
                                                  //| List(9)
                                                  //| List()
                                                  //| List(3, 5, 5)
                                                  //| List(5, 5)
                                                  //| List()
                                                  //| List()
                                                  //| List()
                                                  //| res0: List[Int] = List(22, 20, 9, 8, 7, 5, 5, 3)
}