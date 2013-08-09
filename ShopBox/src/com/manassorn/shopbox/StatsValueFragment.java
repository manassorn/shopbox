package com.manassorn.shopbox;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.TimeChart;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer.FillOutsideLine;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class StatsValueFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TextView text = new TextView(getActivity());
		// text.setText("Hello World!");
		// return text;
		XYMultipleSeriesRenderer renderer = buildRenderer();
		XYMultipleSeriesDataset dataset = getDataset(renderer);
		TimeChart timeChart = new TimeChart(dataset, renderer);
		timeChart.setDateFormat("d MMM");
		GraphicalView graphicalView = new GraphicalView(getActivity(), timeChart);
		
		View root = inflater.inflate(R.layout.fragment_stats_value, null);
		ViewGroup chartContainer = (ViewGroup) root.findViewById(R.id.chart_container);
		chartContainer.addView(graphicalView);
		return root;
	}

	private XYMultipleSeriesRenderer buildRenderer() {
		// renderer.setAxisTitleTextSize(16);
		// renderer.setChartTitleTextSize(20);
		// renderer.setLegendTextSize(15);
		// renderer.setMargins(new int[] { 20, 30, 15, 0 });

		XYSeriesRenderer xyRenderer = new XYSeriesRenderer();
		xyRenderer.setPointStyle(PointStyle.CIRCLE);
		xyRenderer.setColor(0xff3694ca);
		xyRenderer.setFillPoints(true);
		FillOutsideLine fill = new FillOutsideLine(FillOutsideLine.Type.BOUNDS_ABOVE);
		fill.setColor(0xffe8f2fc);
		xyRenderer.addFillOutsideLine(fill);

		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.addSeriesRenderer(xyRenderer);
		renderer.setPointSize(5f);
		renderer.setLabelsTextSize(15);
		renderer.setLabelsColor(Color.BLACK);
		renderer.setPanEnabled(false);
		renderer.setMarginsColor(0xffffffff);
		return renderer;
	}

	private XYMultipleSeriesDataset getDataset(XYMultipleSeriesRenderer renderer) {
		Date[] dateValues = new Date[] { new Date(95, 0, 1), new Date(95, 0, 2),
				new Date(95, 0, 3), new Date(95, 0, 4), new Date(95, 0, 5), new Date(95, 0, 6),
				new Date(95, 0, 7), new Date(95, 0, 8), new Date(95, 0, 9), new Date(95, 0, 10),
				new Date(95, 0, 11), new Date(95, 0, 12), new Date(95, 0, 13), new Date(95, 0, 14),
				new Date(95, 0, 15), new Date(95, 0, 16), new Date(95, 0, 17), new Date(95, 0, 18),
				new Date(95, 0, 19), new Date(95, 0, 20), new Date(95, 0, 21), new Date(95, 0, 22),
				new Date(95, 0, 23), new Date(95, 0, 24), new Date(95, 0, 25) };
		List<Date[]> dates = new ArrayList<Date[]>();
		dates.add(dateValues);

		List<double[]> values = new ArrayList<double[]>();
		values.add(new double[] { 4.9, 5.3, 3.2, 4.5, 6.5, 4.7, 5.8, 4.3, 4, 2.3, 0, 0, 3.2, 5.5,
				4.6, 9.4, 4.3, 1.2, 0, 0.4, 4.5, 3.4, 4.5, 4.3, 4 });

	    String[] titles = new String[] { "Sales growth January 1995 to December 2000" };
	    
		return buildDateDataset(titles, dates, values);
	}

	protected XYMultipleSeriesDataset buildDateDataset(String[] titles, List<Date[]> xValues,
			List<double[]> yValues) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		int length = titles.length;
		for (int i = 0; i < length; i++) {
			TimeSeries series = new TimeSeries(titles[i]);
			Date[] xV = xValues.get(i);
			double[] yV = yValues.get(i);
			int seriesLength = xV.length;
			for (int k = 0; k < seriesLength; k++) {
				series.add(xV[k], yV[k]);
			}
			dataset.addSeries(series);
		}
		return dataset;
	}

}
