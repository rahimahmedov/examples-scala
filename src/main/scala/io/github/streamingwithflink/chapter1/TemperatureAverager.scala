package io.github.streamingwithflink.chapter1

import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector
import io.github.streamingwithflink.util.SensorReading

/** User-defined WindowFunction to compute the average temperature of SensorReadings */
class TemperatureAverager extends WindowFunction[SensorReading, SensorReading, String, TimeWindow] {

  /** apply() is invoked once for each window */
  override def apply(
                      sensorId: String,
                      window: TimeWindow,
                      vals: Iterable[SensorReading],
                      out: Collector[SensorReading]): Unit = {

    // compute the average temperature
    val (cnt, sum) = vals.foldLeft((0, 0.0))((c, r) => (c._1 + 1, c._2 + r.temperature))
    val avgTemp = sum / cnt

    // emit a SensorReading with the average temperature
    out.collect(SensorReading(sensorId, window.getEnd, avgTemp))
  }
}
