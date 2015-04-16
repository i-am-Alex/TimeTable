package com.alex.toggleview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alex.timetable.R;

public class Toggle extends RelativeLayout
{
	protected String leftText = " ";
	protected String rightText = " ";

	protected TextView leftTextView;
	protected TextView rightTextView;

	protected int activeColor;
	protected int nonActiveColor;

	protected int leftDrawable;
	protected int rightDrawable;

	protected Context context;
	protected AttributeSet attrs;

	protected OnStateChangeListener onStateChangeListener;

	protected boolean leftOptionSelected = false;

	public Toggle(Context context)
	{
		super(context);
	}

	public Toggle(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		this.context = context;
		this.attrs = attrs;

		init(context, attrs);
	}

	public Toggle(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.context = context;
		this.attrs = attrs;

		init(context, attrs);
	}

	public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener)
	{
		this.onStateChangeListener = onStateChangeListener;
	}

	public void setLeftText(String leftText)
	{
		this.leftText = leftText;
		if (leftTextView != null)
		{
			leftTextView.setText(this.leftText);
		}
	}

	public void setRightText(String rightText)
	{
		this.rightText = rightText;
		if (rightTextView != null)
		{
			this.rightTextView.setText(rightText);
		}
	}

	public void setActiveColor(int color)
	{
		this.activeColor = color;
	}

	public void setNonActiveColor(int color)
	{
		this.nonActiveColor = color;
	}

	public void setLeftDrawable(int drawable)
	{
		this.leftDrawable = drawable;
	}

	public void setRightDrawable(int drawable)
	{
		this.rightDrawable = drawable;
	}

	protected void init(Context context, AttributeSet attrs)
	{
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (inflater != null)
		{
			inflater.inflate(getView(), this);
		}

		final TypedArray a = getContext().obtainStyledAttributes(attrs,
				R.styleable.Toggle);

		leftText = a.getString(R.styleable.Toggle_leftText);
		rightText = a.getString(R.styleable.Toggle_rightText);

		activeColor = getContext().getResources().getColor(
				R.color.active_toggle_color);
		nonActiveColor = getContext().getResources().getColor(
				R.color.non_active_toggle_color);

		setLeftDrawable(R.drawable.toggle_left);
		setRightDrawable(R.drawable.toggle_right);
	}

	protected int getView()
	{
		return R.layout.toggle;
	}

	protected void initTextViews()
	{
		leftTextView = (TextView) this.findViewById(R.id.left_label);

		if(leftTextView == null) leftTextView = new TextView(this.context);
		leftTextView.setText(leftText);
		leftTextView.setOnClickListener(leftTextOnClickListener);
		
		rightTextView = (TextView) this.findViewById(R.id.right_label);

		if(rightTextView == null) rightTextView = new TextView(this.context);
		rightTextView.setText(rightText);
		rightTextView.setOnClickListener(rightTextOnClickListener);
	}

	@Override
	protected void onFinishInflate()
	{
		super.onFinishInflate();

		initTextViews();

		selectLeft();
	}

	private OnClickListener leftTextOnClickListener = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			selectLeft();
		}
	};

	private OnClickListener rightTextOnClickListener = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			selectRight();
		}
	};

	protected void selectLeft()
	{
		if (!leftOptionSelected)
		{
			this.setBackgroundResource(leftDrawable);
			leftTextView.setTextColor(activeColor);
			rightTextView.setTextColor(nonActiveColor);
			leftOptionSelected = true;

			if (onStateChangeListener != null)
			{
				onStateChangeListener.stateChanged();
			}
		}
	}

	protected void selectRight()
	{
		if (leftOptionSelected)
		{
			this.setBackgroundResource(rightDrawable);
			leftTextView.setTextColor(nonActiveColor);
			rightTextView.setTextColor(activeColor);
			leftOptionSelected = false;

			if (onStateChangeListener != null)
			{
				onStateChangeListener.stateChanged();
			}
		}
	}

	public void setLeftOptionSelected(boolean selected)
	{
		if (selected)
		{
			selectLeft();
		} else {
			selectRight();
		}
	}

	public boolean isLeftOptionSelected()
	{
		return leftOptionSelected;
	}

	public interface OnStateChangeListener
	{
		public void stateChanged();
	}
}
