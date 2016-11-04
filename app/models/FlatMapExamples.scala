package models


object Examples {
  case class Equilibriste(
    gauche: Int = 0,
    droite: Int = 0) {

    def landLeft(nb: Int): Option[Equilibriste] = {
      val n = this.copy(gauche = gauche + nb)
      Some(n).filter(!_.desequilibre)
    }

    def landRight(nb: Int): Option[Equilibriste] = {
      val n = this.copy(droite = droite + nb)
      Some(n).filter(!_.desequilibre)
    }

    def land(step: Step): Option[Equilibriste] = {
        step match {
          case Left(l) => landLeft(l)
          case Right(r) => landRight(r)
        }
    }

    val desequilibre: Boolean = Math.abs(gauche - droite) > 4
  }

  //type Step = (Boolean, Int)
  type Step = Either[Int,Int]
  def sequenceBirds(start: Equilibriste, steps: List[Step]): Option[Equilibriste] = {
    steps match {
      case Nil => Some(start)
      case firstStep :: remainingSteps => {
        val afterFirstStep = start.land(firstStep)
        afterFirstStep.flatMap(eq => {
          sequenceBirds(eq, remainingSteps)
        })
      }
    }

  }

  def seqBirds(eq: Equilibriste, steps: List[Step]) = {
    sequence(Some(eq), steps.map((st: Step) => {
      (eq: Equilibriste) => {
        eq.land(st)
      }
    }))
  }

  def sequence[A](start: Option[A], fs: List[A => Option[A]]): Option[A] = {
    fs.foldLeft(start)((lastValue, f) => {
      lastValue.flatMap(f)
    })
  }


  case class Position(x: Int, y: Int) {
    def moves: List[Position] =
      List(
        Position(x + 2, y - 1), Position(x + 2, y + 1),
        Position(x + 1, y - 2), Position(x + 1, y + 2),
        Position(x - 1, y - 2), Position(x - 1, y + 2),
        Position(x - 2, y - 1), Position(x - 2, y + 1)
      ).filter(p => p.x >= 1 && p.x <= 8 && p.y >= 1 && p.y <= 8)

    def threeMoves: List[Position] = {
      val afterFirstMove = this.moves
      val afterSecond = afterFirstMove.flatMap(p => p.moves)
      val afterThird = afterSecond.flatMap(p => p.moves)
      afterThird.distinct
    }

    def canReachInThree(p: Position): Boolean = threeMoves.contains(p)
  }
}
