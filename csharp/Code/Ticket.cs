using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Runtime.Remoting.Messaging;
using System.Threading.Tasks;
using System.Diagnostics;

namespace TicketSystem
{
    class Ticket
    {
        private int count =100;

        public int Count
        {
            get
            {
                return this.count;
            }
        }

        public string GetTicket()
        {
            //while (true)
            //{
                this.count++;
                Thread.Sleep(50);
                this.count--;
            //}
            return "G" + this.count--;
        }
    }

    class Person
    {
        private string name, id;

        private int age;

        public string Name
        {
            get
            {
                return this.name;
            }
            set
            {
                if (value.Length > 0 && value.Length < 8)
                {
                    this.name = value;
                }
                else
                {
                    throw new IndexOutOfRangeException("Length of name is out of 0~8.");
                }
            }
        }

        public int Age
        {
            get
            {
                return this.age;
            }
            set
            {
                if (value > 0)
                {
                    this.age = value;
                }
                else
                {
                    throw new IndexOutOfRangeException("Age must be more than 0.");
                }
            }
        }

        public string ID
        {
            get
            {
                return this.id;
            }
            set
            {
                if (value.Length == 18)
                {
                    this.id = value;
                }
                else
                {
                    throw new IndexOutOfRangeException("Lengh of ID must be 16.");
                }
            }
        }

        public Person(string nameOfPerson, int ageOfPerson, string idOfPerson)
        {
            this.name = nameOfPerson;
            this.age = ageOfPerson;
            this.id = idOfPerson;
        }
    }
        
    /// <summary>
    /// 通过ThreadStart来创建一个新线程是最直接的方法，但这样创建出来的线程比较难管理，
    /// 如果创建过多的线程反而会让系统的性能下载。有见及此，.NET为线程管理专门设置了一个CLR线程池，
    /// 使用CLR线程池系统可以更合理地管理线程的使用。所有请求的服务都能运行于线程池中，
    /// 当运行结束时线程便会回归到线程池。通过设置，能控制线程池的最大线程数量，在请求超出线程最大值时，
    /// 线程池能按照操作的优先级别来执行，让部分操作处于等待状态，待有线程回归时再执行操作。
    /// </summary>
    class Program
    {
        static void BuyTicket(object state)
        {
            Ticket newTic = (Ticket)state;
            BuyTicket(newTic);            
        }

        static string BuyTicket(Ticket newTic)
        {
            lock (newTic)
            {
                ThreadMessage("Async Thread start:");
                Console.WriteLine("Async thread do work!");
                string message = newTic.GetTicket();
                Console.WriteLine(message + "\n");
                return message;
            }
        }

        //显示线程Id
static void ThreadMessage(string data)
{
    string message = string.Format("{0}\nCurrentThreadId is {1}", data, Thread.CurrentThread.ManagedThreadId);
    Console.WriteLine(message);
} 

        #region 通过Thread类实现多线程
        /// ParameterizedThreadStart委托与ThreadStart委托非常相似，
        /// 但ParameterizedThreadStart委托是面向带参数方法的。注意ParameterizedThreadStart 
        /// 对应方法的参数为object，此参数可以为一个值对象，也可以为一个自定义对象。        
        static void Main1(string[] args)
        {
            Ticket tic = new Ticket();
            Person[] person = new Person[10]
                {
                    new Person("Nicholas", 21, "000000000000000000"),
                    new Person("Nate", 38, "111111111111111111"),
                    new Person("Vincent", 21, "222222222222222222"),
                    new Person("Niki", 51, "333333333333333333"),
                    new Person("Gary", 28, "444444444444444444"),
                    new Person("Charles", 49, "555555555555555555"),
                    new Person("Karl ", 55, "666666666666666666"),
                    new Person("Katharine", 19, "777777777777777777"),
                    new Person("Lee", 25, "888888888888888888"),
                    new Person("Ann", 34, "99999999999999999"),
                };

            ThreadMessage("MainThread start");
            Console.WriteLine();
            Thread[] t = new Thread[person.Length];

            for (int i = 0; i < person.Length; i++)
            {
                t[i] = new Thread(new ParameterizedThreadStart(BuyTicket));
                t[i].Start(tic);                   
            }

            for (int i = 0; i < 3; i++)
            {
                Console.WriteLine("Main thread do work!");
                Thread.Sleep(200);
            }

            Console.ReadKey();
        }        
        #endregion

        #region 通过线程池CLR实现多线程
        ///使用ThreadStart与ParameterizedThreadStart建立新线程非常简单，
        ///但通过此方法建立的线程难于管理，若建立过多的线程反而会影响系统的性能。
        ///当启动一个线程时，会有几百毫秒的时间花费在准备一些额外的资源上，例如一个新的私有局部变量栈这样的事情。
        ///每个线程会占用（默认情况下）1MB 内存。因此.NET引入CLR线程池这个概念。
        ///
        ///线程池（thread pool）可以通过共享与回收线程来减轻这些开销，
        ///允许多线程应用在很小的粒度上而没有性能损失。
        ///CLR线程池并不会在CLR初始化的时候立刻建立线程，
        ///而是在应用程序要创建线程来执行任务时，线程池才初始化一个线程。
        ///线程的初始化与其他的线程一样。在完成任务以后，该线程不会自行销毁，
        ///而是以挂起的状态返回到线程池。直到应用程序再次向线程池发出请求时，
        ///线程池里挂起的线程就会再度激活执行任务。这样既节省了建立线程所造成的性能损耗，
        ///也可以让多个任务反复重用同一线程，从而在应用程序生存期内节约大量开销。
        ///CLR线程池分为工作者线程（workerThreads）与I/O线程 (completionPortThreads) 两种，
        ///工作者线程是主要用作管理CLR内部对象的运作，I/O（Input/Output) 线程顾名思义是用于与外部系统交换信息，

        #region 通过QueueUserWorkItem使用线程池
        static void Main(string[] args)
        {
            Ticket tic = new Ticket();
            Person[] person = new Person[10]
                {
                    new Person("Nicholas", 21, "000000000000000000"),
                    new Person("Nate", 38, "111111111111111111"),
                    new Person("Vincent", 21, "222222222222222222"),
                    new Person("Niki", 51, "333333333333333333"),
                    new Person("Gary", 28, "444444444444444444"),
                    new Person("Charles", 49, "555555555555555555"),
                    new Person("Karl ", 55, "666666666666666666"),
                    new Person("Katharine", 19, "777777777777777777"),
                    new Person("Lee", 25, "888888888888888888"),
                    new Person("Ann", 34, "99999999999999999"),
                };

            ThreadPool.SetMaxThreads(1000, 1000);
            ThreadPool.SetMinThreads(2, 2);

            ThreadMessage("MainThread start");
            Console.WriteLine();
            foreach(Person someone in person)
            {
                ThreadPool.QueueUserWorkItem(new WaitCallback(BuyTicket), tic);
            }
            
            for (int i = 0; i < 3; i++)
            {
                Console.WriteLine("Main thread do work!");
                Thread.Sleep(200);
            }

            Console.ReadKey();
        }        
        #endregion


        /// 通过ThreadPool.QueueUserWorkItem启动工作者线程虽然是方便，
        /// 但WaitCallback委托指向的必须是一个带有Object参数的无返回值方法，
        /// 这无疑是一种限制。若方法需要有返回值，或者带有多个参数，这将多费周折。
        /// 有见及此，.NET提供了另一种方式去建立工作者线程，那就是委托。  
        #region 通过委托使用线程池
        delegate string MyDelegate(Ticket tic);//可以带多个参数

        static void Main3(string[] args)
        {
            Ticket tic = new Ticket();
            Person[] person = new Person[10]
                {
                    new Person("Nicholas", 21, "000000000000000000"),
                    new Person("Nate", 38, "111111111111111111"),
                    new Person("Vincent", 21, "222222222222222222"),
                    new Person("Niki", 51, "333333333333333333"),
                    new Person("Gary", 28, "444444444444444444"),
                    new Person("Charles", 49, "555555555555555555"),
                    new Person("Karl ", 55, "666666666666666666"),
                    new Person("Katharine", 19, "777777777777777777"),
                    new Person("Lee", 25, "888888888888888888"),
                    new Person("Ann", 34, "99999999999999999"),
                };
            ThreadPool.SetMaxThreads(1000, 1000);
            ThreadPool.SetMinThreads(2, 2);

            ThreadMessage("MainThread start");
            Console.WriteLine();
            foreach (Person someone in person)
            {
                MyDelegate myDelegate = new MyDelegate(BuyTicket);
                myDelegate.BeginInvoke(tic, new AsyncCallback(Completed), someone);
            }

            for (int i = 0; i < 3; i++)
            {
                Console.WriteLine("Main thread do work!");
                Thread.Sleep(200);
            }

            Console.ReadKey();
        }              

        static void Completed(IAsyncResult result)
        {
            Console.WriteLine();
            ThreadMessage("Async Completed");
            //获取委托对象，调用EndInvoke方法获取运行结果
            AsyncResult _result = (AsyncResult)result;
            MyDelegate myDelegate = (MyDelegate)_result.AsyncDelegate;
            string data = myDelegate.EndInvoke(_result);

            //获取Person对象
            Person person = (Person)result.AsyncState;
            Console.WriteLine("Person name is " + person.Name);
            Console.WriteLine("Person age is " + person.Age);
            Console.WriteLine("Person ID is " + person.ID);
            Console.WriteLine("Tick ID is "+ data);
            Console.WriteLine();
        }
        #endregion 
        #endregion

        #region 通过TPL实现多线程
        ///从 .NET Framework 4 开始，TPL 是编写多线程代码和并行代码的首选方法
        /// Parallel类实现
        static void Main4(string[] args)
        {
            Ticket tic = new Ticket();
            Person[] person = new Person[10]
                {
                    new Person("Nicholas", 21, "000000000000000000"),
                    new Person("Nate", 38, "111111111111111111"),
                    new Person("Vincent", 21, "222222222222222222"),
                    new Person("Niki", 51, "333333333333333333"),
                    new Person("Gary", 28, "444444444444444444"),
                    new Person("Charles", 49, "555555555555555555"),
                    new Person("Karl ", 55, "666666666666666666"),
                    new Person("Katharine", 19, "777777777777777777"),
                    new Person("Lee", 25, "888888888888888888"),
                    new Person("Ann", 34, "99999999999999999"),
                };
            Stopwatch watch = new Stopwatch();

            ThreadMessage("MainThread start");
            Console.WriteLine();

            watch.Start();
            Parallel.For(0, person.Length, item =>
                {
                    BuyTicket(tic);
                });
            watch.Stop();
            Console.WriteLine("Parallel running need " + watch.ElapsedMilliseconds + " ms."); 

            for (int i = 0; i < 3; i++)
            {
                Console.WriteLine("Main thread do work!");
                Thread.Sleep(200);
            }

            Console.ReadKey();
        }

        /// 通过Task类实现
        /// 如果你熟悉旧式的构造，可以将非泛型的Task类看作ThreadPool.QueueUserWorkItem的替代，
        /// 而泛型的Task<TResult>看作异步委托的替代。
        /// 比起旧式的构造，新式的构造会更快速，更方便，并且更灵活。
        static void Main5(string[] args)
        {
            Ticket tic = new Ticket();
            Person[] person = new Person[10]
                {
                    new Person("Nicholas", 21, "000000000000000000"),
                    new Person("Nate", 38, "111111111111111111"),
                    new Person("Vincent", 21, "222222222222222222"),
                    new Person("Niki", 51, "333333333333333333"),
                    new Person("Gary", 28, "444444444444444444"),
                    new Person("Charles", 49, "555555555555555555"),
                    new Person("Karl ", 55, "666666666666666666"),
                    new Person("Katharine", 19, "777777777777777777"),
                    new Person("Lee", 25, "888888888888888888"),
                    new Person("Ann", 34, "99999999999999999"),
                };
            Stopwatch watch = new Stopwatch();

            ThreadPool.SetMaxThreads(1000, 1000);
            ThreadPool.SetMinThreads(2, 2);
            ThreadMessage("MainThread start");
            Console.WriteLine();
            Dictionary<Person, string> result = new Dictionary<Person, string>();
            Task<string>[] tasks = new Task<string>[person.Length];
            
            watch.Start();
            for (int i = 0; i < person.Length; i++)
            {
                tasks[i] = Task.Factory.StartNew<string>(() => (BuyTicket(tic)));                
            }
            Task.WaitAll(tasks, 5000);//设置超时5s
            watch.Stop();
            Console.WriteLine("Tasks running need " + watch.ElapsedMilliseconds + " ms." + "\n"); 
          
            for (int i = 0; i < tasks.Length; i++)
            {
                //超时处理
                if (tasks[i].Status != TaskStatus.RanToCompletion)
                {
                    Console.WriteLine("Task {0} Error!", i + 1);
                }
                else
                {
                    //save result
                    result.Add(person[i], tasks[i].Result);
                    Console.WriteLine("Person name is " + person[i].Name);
                    Console.WriteLine("Person age is " + person[i].Age);
                    Console.WriteLine("Person ID is " + person[i].ID);
                    Console.WriteLine("Tick ID is " + tasks[i].Result);
                    Console.WriteLine();
                }
            }

            for (int i = 0; i < 3; i++)
            {
                Console.WriteLine("Main thread do work!");
                Thread.Sleep(200);
            }

            Console.ReadKey();
        }
        #endregion
    }
}
