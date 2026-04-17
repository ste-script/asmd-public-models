package u07.examples

import u07.modelling.{CTMC, SPN}
import u07.utils.MSet
import java.util.Random

object ReadersAndWriters extends App:
  // Specification of my data-type for states
  enum Place:
    case IDLE, ROUTER, R_QUEUE, W_QUEUE, LOCK, READING, WRITING

  export Place.*
  export u07.modelling.CTMCSimulation.*
  export u07.modelling.SPN.*

  val spn = SPN[Place](
    Trn(MSet(IDLE), m => 1.0, MSet(ROUTER), MSet()), // t1
    Trn(MSet(ROUTER), m => 200000.0, MSet(R_QUEUE), MSet()), // t2
    Trn(MSet(ROUTER), m => 100000.0, MSet(W_QUEUE), MSet()), // t3
    Trn(
      MSet(R_QUEUE, LOCK),
      m => 100000.0,
      MSet(LOCK, READING),
      MSet()
    ), // t4
    Trn(
      MSet(W_QUEUE, LOCK),
      m => 100000.0,
      MSet(WRITING),
      MSet(READING)
    ), // t5
    Trn(MSet(READING), m => 0.1, MSet(IDLE), MSet()), // t6
    Trn(MSet(WRITING), m => 0.2, MSet(LOCK, IDLE), MSet()) // t7
  )

  println:
    toCTMC(spn)
      .newSimulationTrace(
        MSet(IDLE, IDLE, IDLE, IDLE, IDLE, IDLE, IDLE, IDLE, IDLE, IDLE, LOCK),
        new Random
      )
      .take(100)
      .toList
      .mkString("\n")
