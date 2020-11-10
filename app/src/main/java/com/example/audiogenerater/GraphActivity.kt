package com.example.audiogenerater

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries

class GraphActivity : AppCompatActivity() {
    private var series: LineGraphSeries<DataPoint>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)

        val buffer = intent.getShortArrayExtra("q")

        val graph = findViewById<GraphView>(R.id.graph1)
        series = LineGraphSeries()
        repeat(5000) {index ->
            series!!.appendData(DataPoint(index.toDouble(), buffer[index].toDouble()), true, 5000)
        }
        graph.addSeries(series)
    }
}