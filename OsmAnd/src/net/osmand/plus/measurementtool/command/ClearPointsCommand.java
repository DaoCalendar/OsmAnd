package net.osmand.plus.measurementtool.command;

import net.osmand.GPXUtilities.WptPt;
import net.osmand.plus.measurementtool.MeasurementToolLayer;

import java.util.ArrayList;
import java.util.List;

public class ClearPointsCommand extends MeasurementModeCommand {

	private List<WptPt> points;
	private ClearCommandMode clearMode;
	private int pointPosition;

	public enum ClearCommandMode {
		ALL,
		BEFORE,
		AFTER
	}

	public ClearPointsCommand(MeasurementToolLayer measurementLayer, ClearCommandMode clearMode) {
		super(measurementLayer);
		this.clearMode = clearMode;
	}

	@Override
	public boolean execute() {
		pointPosition = getEditingCtx().getSelectedPointPosition();
		executeCommand();
		return true;
	}

	private void executeCommand() {
		List<WptPt> pts = getEditingCtx().getPoints();
		points = new ArrayList<>(pts);
		switch (clearMode) {
			case ALL:
				pts.clear();
				getEditingCtx().clearSegments();
				break;
			case BEFORE:
				getEditingCtx().trimBefore(pointPosition);
				break;
			case AFTER:
				getEditingCtx().trimAfter(pointPosition);
		}
		refreshMap();
	}

	@Override
	public void undo() {
		getEditingCtx().clearSegments();
		getEditingCtx().addPoints(points);
		getEditingCtx().updateCacheForSnap();
		refreshMap();
	}

	@Override
	public void redo() {
		executeCommand();
	}

	@Override
	public MeasurementCommandType getType() {
		return MeasurementCommandType.CLEAR_POINTS;
	}
}
