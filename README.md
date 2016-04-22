# TextWave

## 介绍 ##

本项目实现了文字的一个动画效果：波浪，如下图所示：

![](http://i.imgur.com/CPSZPLG.gif)

## 使用方法 ##

**在build.gradle中添加：**

	compile 'com.dalu9527:TextWave:0.0.1'


**在布局中使用：**

	<com.wave.library.view.WaveTextView
	        android:id="@+id/waveTextView"
	        android:layout_width="wrap_content"
	        android:layout_height="wrap_content"
	        wave:textColor="@color/colorAccent"
	        wave:textSize="17sp"
	        wave:waveSpeed="1"
	        wave:waveAmplitudes="20"
			wave:text="Hello,World"
			wave:autoStart="true"
	        android:gravity="center"
	        />

其中属性

- wave:textColor="@color/colorAccent" 表示：文字的颜色
- wave:textSize="17sp" 表示：文字的大小
- wave:waveSpeed="1" 表示：文字波浪起伏的速度
- wave:waveAmplitudes="20" 表示：波浪的振幅（不能超过布局的高度）
- wave:text="Hello,World" 表示：需要飘逸的文字
- wave:autoStart="true" 表示： 是否飘动,表示true时，不需要代码里面设置waveTextView.startWave();

**在 Activity or Fragment 中使用：**

 	WaveTextView waveTextView = (WaveTextView)findViewById(R.id.waveTextView);
    waveTextView.setString(getResources().getString(R.string.test));// 设置文字，xml设置text后，这里不需要设置
    waveTextView.startWave();// 开启波浪效果
    //waveTextView.stopWave();// 关闭波浪效果

## Introduce ##

This project implements an animation of the text：wave. As shown in the figure below:

![](http://i.imgur.com/CPSZPLG.gif)

## Usage ##

**In build.gradle add：**

	compile 'com.dalu9527:TextWave:0.0.1'

**In Your Layout：**

	<com.wave.library.view.WaveTextView
	        android:id="@+id/waveTextView"
	        android:layout_width="wrap_content"
	        android:layout_height="wrap_content"
	        wave:textColor="@color/colorAccent"
	        wave:textSize="17sp"
	        wave:waveSpeed="1"
	        wave:waveAmplitudes="20"
			wave:text="Hello,World"
			wave:autoStart="true"
	        android:gravity="center"
	        />

attrs:

- wave:textColor="@color/colorAccent" Text's color
- wave:textSize="17sp" Text's size
- wave:waveSpeed="1" Text's wave speed
- wave:waveAmplitudes="20" Text's wave amplitudes
- wave:text="Hello,World" Your Text
- wave:autoStart="true" auto start move

**In Your Activity or Fragment：**

 	WaveTextView waveTextView = (WaveTextView)findViewById(R.id.waveTextView);
    waveTextView.setString(getResources().getString(R.string.test));// set your text，if you set in xml，just not use this
    waveTextView.startWave();// start the wave
    //waveTextView.stopWave();// stop the wave

