# chart控件的使用

本文介绍如何使用工具箱里的chart控件，绘制多条曲线。效果图如下：

![](http://img.blog.csdn.net/20160307112019936)

**1.InitializeChart**

在窗体里添加chart控件，然后在属性里清空ChartAreas、Legends和Series集合，它们会由下面代码动态实现。在窗体构造函数里，实现InitializeChart和DrawSeries方法。

InitializeChart代码如下。

```csharp

public partial class MainFormBERT : Form
    {
        public MainFormBERT()
        {
            InitializeComponent();
            InitializeChart();
            DrawSeries();
        }

         public void InitializeChart()
        {
            #region 设置图表的属性
            //图表的背景色
            chart1.BackColor = Color.FromArgb(211, 223, 240);
            //图表背景色的渐变方式
            chart1.BackGradientStyle = GradientStyle.None;
            //图表的边框颜色、
            chart1.BorderlineColor = Color.FromArgb(26, 59, 105);
            //图表的边框线条样式
            chart1.BorderlineDashStyle = ChartDashStyle.Solid;
            //图表边框线条的宽度
            chart1.BorderlineWidth = 2;
            //图表边框的皮肤
            chart1.BorderSkin.SkinStyle = BorderSkinStyle.None;
            #endregion

            #region 设置图表的Title
            Title title = new Title();
            //标题内容
            title.Text = "BER";
            //标题的字体
            title.Font = new System.Drawing.Font("Microsoft Sans Serif", 12, FontStyle.Regular);
            //标题字体颜色
            //title.ForeColor = Color.FromArgb(26, 59, 105);
            //标题阴影颜色
            //title.ShadowColor = Color.FromArgb(32, 0, 0, 0);
            //标题阴影偏移量
            //title.ShadowOffset = 3;

            chart1.Titles.Add(title);
            #endregion

            #region 设置图表区属性
            //图表区的名字
            ChartArea chartArea = new ChartArea("Default");
            //背景色
            chartArea.BackColor = Color.White;// Color.FromArgb(64, 165, 191, 228);
            //背景渐变方式
            chartArea.BackGradientStyle = GradientStyle.None;
            //渐变和阴影的辅助背景色
            chartArea.BackSecondaryColor = Color.White;
            //边框颜色
            chartArea.BorderColor = Color.Blue;
            //边框线条宽度
            chartArea.BorderWidth = 2;
            //边框线条样式
            chartArea.BorderDashStyle = ChartDashStyle.Solid;
            //阴影颜色
            //chartArea.ShadowColor = Color.Transparent;

            //设置X轴和Y轴线条的颜色和宽度
            chartArea.AxisX.LineColor = Color.FromArgb(64, 64, 64, 64);
            chartArea.AxisX.LineWidth = 1;
            chartArea.AxisY.LineColor = Color.FromArgb(64, 64, 64, 64);
            chartArea.AxisY.LineWidth = 1;
             
            //设置X轴和Y轴的标题
            //chartArea.AxisX.Title = "time";
            //chartArea.AxisY.Title = "count";
            //chartArea.AxisX.TitleFont = new System.Drawing.Font("Microsoft Sans Serif", 10, FontStyle.Regular);
            //chartArea.AxisY.TitleFont = new System.Drawing.Font("Microsoft Sans Serif", 10, FontStyle.Regular);

            //设置图表区网格横纵线条的颜色和宽度
            chartArea.AxisX.MajorGrid.LineColor = Color.FromArgb(64, 64, 64, 64);
            chartArea.AxisX.MajorGrid.LineWidth = 1;
            chartArea.AxisY.MajorGrid.LineColor = Color.FromArgb(64, 64, 64, 64);
            chartArea.AxisY.MajorGrid.LineWidth = 1;          

            chart1.ChartAreas.Add(chartArea);
            #endregion

            #region 图例及图例的位置
            Legend legend = new Legend();
            legend.Alignment = StringAlignment.Center;
            legend.Docking = Docking.Bottom;
            legend.BackColor = Color.Transparent;

            this.chart1.Legends.Add(legend);
            #endregion
        }
  }
```

**2.设置曲线样式**

类型、颜色和宽度等。

```csharp

private Series SetSeriesStyle(int i)
        {
            Series series = new Series(string.Format("Ch{0}", i + 1));

            //Series的类型
            series.ChartType = SeriesChartType.Line;
            //Series的边框颜色
            series.BorderColor = Color.FromArgb(180, 26, 59, 105);
            //线条宽度
            series.BorderWidth = 3;
            //线条阴影颜色
            //series.ShadowColor = Color.Black;
            //阴影宽度
            //series.ShadowOffset = 2;
            //是否显示数据说明
            series.IsVisibleInLegend = true;
            //线条上数据点上是否有数据显示
            series.IsValueShownAsLabel = false;
            //线条上的数据点标志类型
            series.MarkerStyle = MarkerStyle.None;
            //线条数据点的大小
            //series.MarkerSize = 8;
            //线条颜色
            switch (i)
            {
                case 0:
                    series.Color = Color.FromArgb(220, 65, 140, 240);
                    break;
                case 1:
                    series.Color = Color.FromArgb(220, 224, 64, 10);
                    break;
                case 2:
                    series.Color = Color.FromArgb(220, 120, 150, 20);
                    break;
                case 3:
                    series.Color = Color.FromArgb(220, 12, 128, 232);
                    break;
            }
            return series;
        }
```

**3.绘制曲线**

从datatable里获取数据，绘制四条曲线。

```csharp

//绘制曲线
     private void DrawSeries()
        {
            dt = new TestDataTable();
            dt.CreateTable();

            for (int i = 0; i < 4; i++)
            {
                Series series = this.SetSeriesStyle(i);
                DataRow[] foundRows;
                string expression = "Ch = " + i;
                foundRows = dt.Select(expression);
                foreach (DataRow row in foundRows)
                {
                    series.Points.AddXY(row[0], row[2]);
                }
                this.chart1.Series.Add(series);
            }
        }
```

**4.显示或隐藏曲线**

根据checkbox的状态显示和隐藏这四条曲线。

```csharp

//显示隐藏曲线
     private void DisOrPlaySeries(int i)
        {
            if (checkBox[i].Checked)
            {
                DataRow[] foundRows;
                string expression = "Ch = " + i;
                foundRows = dt.Select(expression);
                foreach (DataRow row in foundRows)
                {
                    this.chart1.Series[i].Points.AddXY(row[0], row[2]);
                }
            }
            else
            {
                this.chart1.Series[i].Points.Clear();
            }
        }
```