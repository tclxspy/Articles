# 以流形式将数据保存到本地

主要介绍DataTable、日志Log、文件流FileStream、StreamWriter类，将数据保存为txt和excel格式。

**1.创建TestDataTable类**

CreateTable()方法创建一个数据table，SaveTableToExcel(string fileName)方法将数据table存为文件名为fileName的excel格式文件，CreateDirectory(string fileName)方法去检查文件是否存在，若不存在，则自动创建一个。

```csharp

class TestDataTable: DataTable
    {
        public void CreateTable()
        {
            this.Columns.Add("Time(s)", System.Type.GetType("System.String"));
            this.Columns.Add("Ch", System.Type.GetType("System.String"));
            this.Columns.Add("BER", System.Type.GetType("System.String"));

            const int length1 = 4;
            const int length2 = 10;

            int[][] data = new int[length1][];
            //第一条数据
            data[0] = new int[length2] { 13, 25, 21, 33, 28, 39, 43, 36, 42, 36 };
            //第二条数据
            data[1] = new int[length2] { 20, 13, 10, 5, 15, 7, 10, 14, 19, 20 };
            //第三条数据
            data[2] = new int[length2] { 78, 92, 65, 83, 90, 59, 63, 72, 88, 98 };
            //第四条数据
            data[3] = new int[length2] { 45, 49, 39, 47, 52, 76, 67, 51, 57, 67 };

            for (int i = 0; i < length2; i++)
            {
                for (int j = 0; j < length1; j++)
                {
                    DataRow dr = this.NewRow();
                    dr[0] = i + 1;
                    dr[1] = j;
                    dr[2] = data[j][i];
                    this.Rows.Add(dr);
                }
            }
        }

        public void SaveTableToExcel(string fileName)
        {
            CreateDirectory(fileName);
            StringBuilder title = new StringBuilder();
            FileStream fileStream = new FileStream(fileName, FileMode.OpenOrCreate);
            StreamWriter writer = new StreamWriter(new BufferedStream(fileStream), System.Text.Encoding.Default);

            for (int i = 0; i < this.Columns.Count; i++)
            {
                title.Append(this.Columns[i].ColumnName + "\t"); //栏位：自动跳到下一单元格
            }

            title.Append("\n");
            writer.Write(title);

            foreach (DataRow row in this.Rows)
            {
                StringBuilder content = new StringBuilder();
                for (int i = 0; i < this.Columns.Count; i++)
                {
                    content.Append(row[i] + "\t");//内容：自动跳到下一单元格
                }
                content.Append("\n");
                writer.Write(content);
            }
            writer.Close();
            fileStream.Close();
        }

        public void CreateDirectory(string fileName)
        {
            DirectoryInfo directoryInfo = Directory.GetParent(fileName);
            if (!directoryInfo.Exists)
            {
                directoryInfo.Create();
            }
        }  
    }
```

**2.创建日志Log类**

将文件名FileName定义为类的属性，在构造函数时赋值。SaveLogToTxt(string info)方法将数据info存入文件名为fileName的txt文件中。同样CreateDirectory()方法去检查文件是否存在。

```csharp

class Log
    {
        private string fileName; 

        public string FileName
        {
            set
            {
                this.fileName = value;
            }
            get
            {
                return this.fileName;
            }
        }

        public Log(string fileName)
        {
            this.fileName = fileName;
            CreateDirectory();
        }  
  
        public void SaveLogToTxt(string info)  
        {    
            StreamWriter writer = null;  
            FileStream fileStream = null;

            try  
            {
                System.IO.FileInfo fileInfo = new System.IO.FileInfo(this.fileName);  
                if (!fileInfo.Exists)  
                {  
                    fileStream = fileInfo.Create();  
                    writer = new StreamWriter(fileStream);  
                }  
                else  
                {  
                    fileStream = fileInfo.Open(FileMode.Append, FileAccess.Write);  
                    writer = new StreamWriter(fileStream);  
                }  
                writer.WriteLine(info);
            }  
            finally  
            {  
                if (writer != null)  
                {  
                    writer.Close();  
                    writer.Dispose();  
                    fileStream.Close();  
                    fileStream.Dispose();  
                }  
            }  
        }  
  
        public void CreateDirectory()  
        {  
            DirectoryInfo directoryInfo = Directory.GetParent(this.fileName);  
            if (!directoryInfo.Exists)  
            {  
                directoryInfo.Create();  
            }  
        }  
    }  
```

**3.调用类方法存数据**

实例化类、创建数据table，将table保存为txt格式和excel格式，以时间命名。

```csharp

TestDataTable dt = new TestDataTable();
dt.CreateTable();
dt.Rows.Clear(); //清除数据table里所有行

DateTime startTime = DateTime.Now;
Log log = new Log(@"Log\" + startTime.ToString("yyyyMMddHHmmss") + ".txt");//以时间为文件命名，存在exe所在文件夹下的Log文件夹里

for (int i = 0; i < 4; i++)
{
    DataRow dr = dt.NewRow();
    dr[0] = (int)second;
    dr[1] = i;
    dr[2] = BERByChannel[i].ToString("E2");
    dt.Rows.Add(dr);//添加新行到数据table
                 
    string info = string.Format("{0}: Time(s): {1}, Ch: {2}, BER: {3}", DateTime.Now, dr[0], dr[1], dr[2]);
    log.SaveLogToTxt(info);//存为txt格式                    
}

//存为excel格式
string fileName = @"TestRecord\" + startTime.ToString("yyyyMMddHHmmss") + ".xls";
dt.SaveTableToExcel(fileName);//以时间为文件命名，存在exe所在文件夹下的TestRecord文件夹里
```