package com.manassorn.shopbox;

import java.util.Date;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer.FillOutsideLine;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class StatsTop5Fragment extends Fragment {
	private static int[] COLORS = new int[] { 0xff4ac0c0, 0xff548dca, 0xffd2e27a, 0xffef366d,
			0xfffdb93e };
	private static int[] values = new int[] { 10, 20, 30, 40, 50 };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		DefaultRenderer renderer = buildRenderer();
		CategorySeries series = getCategorySeries(renderer);
		GraphicalView graphicalView = ChartFactory.getPieChartView(getActivity(), series, renderer);

		View root = inflater.inflate(R.layout.fragment_stats_top5, null);
		ViewGroup chartContainer = (ViewGroup) root.findViewById(R.id.chart_container);
		chartContainer.addView(graphicalView);
		ListView top5ListView = (ListView) root.findViewById(R.id.top5_list);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				R.layout.top5_list_item, R.id.name, new String[] { "12% - Series 1", "13% - Series 2", "Series 3", "Series 4", "Series 5", "Series 6" });
		top5ListView.setAdapter(adapter);

		return root;
	}

	private DefaultRenderer buildRenderer() {
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

		DefaultRenderer renderer = new DefaultRenderer();
		// XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		// renderer.addSeriesRenderer(xyRenderer);
		// renderer.setPointSize(5f);
		// renderer.setLabelsTextSize(15);
		// renderer.setLabelsColor(Color.BLACK);
		// renderer.setPanEnabled(false);
		// renderer.setMarginsColor(0xffffffff);
		renderer.setZoomButtonsVisible(false);
		renderer.setStartAngle(270);
		renderer.setDisplayValues(true);
		renderer.setPanEnabled(false);
		// renderer.setApplyBackgroundColor(true);
		// renderer.setBackgroundColor(0xff00ff00);
		renderer.setShowLegend(false);
		renderer.setLabelsTextSize(20);
		// renderer.setLabelsColor(0xffffffff);

		return renderer;
	}

	private CategorySeries getCategorySeries(DefaultRenderer renderer) {

		CategorySeries series = new CategorySeries("");

		for (int value : values) {
			series.add("Series " + (series.getItemCount() + 1), value);
			SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
			seriesRenderer.setHighlighted(true);
			seriesRenderer.setColor(COLORS[(series.getItemCount() - 1) % COLORS.length]);
			renderer.addSeriesRenderer(seriesRenderer);

		}
		// mChartView.repaint();

		return series;
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
