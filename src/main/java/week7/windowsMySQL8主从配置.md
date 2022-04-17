# windows下mysql8主从异步配置

@[toc]
## 一、mysql8下载

官网下载地址[MySQL Community Downloads](https://dev.mysql.com/downloads/)(https://dev.mysql.com/downloads/mysql/)选择对应的操作系统和下载的包，本示例选用windows操作系统，下载zip包。

![在这里插入图片描述](https://img-blog.csdnimg.cn/1a09309e364848329d5ddc8943408695.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBATHZRaUZlbg==,size_20,color_FFFFFF,t_70,g_se,x_16)

下载完成后，直接解压即可。解压后，额外复制2份，1主2从，为了区分，文件名可做更改。

![在这里插入图片描述](https://img-blog.csdnimg.cn/354f38a2728c42e8b400ba99d191e624.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBATHZRaUZlbg==,size_20,color_FFFFFF,t_70,g_se,x_16)

### 注意事项

- mysql8的zip包安装没有my.ini 启动配置文件，需要自己在bin目录的同一层级自己创建输入相关参数
- data目录也就是数据库对应的数据存放文件不能自己创建需要用初始化命令自动生成

## 二、主服务配置

> 要自己在bin目录的同一层级自己创建输入相关参数

关键命令参考：

- 安装服务： `mysqld -install 服务名称`

- 初始化data文件和临时密码 ：`mysqld --initialize --user=mysql --console`
- 启动mysql服务：`net start 服务名称`
- 关闭mysql服务： `net stop 服务名称`
- 连接mysql 服务： `mysql -u 用户名 -p`
- 修改用户密码：`ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '123456';`

### （一）my.ini文件

> 要自己在 mysql-8-master 里的 bin目录的同一层级 自己手动创建my.ini文件。 核心参数：如下参考，后面有全的。

```ini
[mysqld]
#设置服务端端口
port=33061
#MySQL安装路径
basedir=D:\mysql\mysql-8-master
#MySQL数据存放路径
datadir=D:\mysql\mysql-8-master\data
#连接到服务器时使用的默认认证插件
default_authentication_plugin=caching_sha2_password
#当创建新表时将使用的默认存储引擎  
default-storage-engine=INNODB
#二进制binglog Binary Logging.
log-bin="mysql-master-bin"
# 服务ID
server-id=1
# Set the SQL mode to strict
sql-mode="STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION"
```

> 进入 mysql-8-master 目录创建 my.ini文件 全文输入如下内容

```java
[client]

port=33061

[mysql]
no-beep

# default-character-set=
[mysqld]
    
#设置服务端端口
port=33061

#MySQL安装路径
basedir=D:\mysql\mysql-8-master

#MySQL数据存放路径
datadir=D:\mysql\mysql-8-master\data

#连接到服务器时使用的默认认证插件
default_authentication_plugin=caching_sha2_password

#当创建新表时将使用的默认存储引擎  
default-storage-engine=INNODB

# Set the SQL mode to strict
sql-mode="STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION"

# General and Slow logging.
log-output=FILE
general-log=0
general_log_file="DESKTOP-40R603D.log"
slow-query-log=1
slow_query_log_file="DESKTOP-40R603D-slow.log"
long_query_time=10

# Binary Logging.
log-bin="mysql-master-bin"

# Error Logging.
log-error="DESKTOP-40R603D.err"

# Server Id.
# 服务ID
server-id=1

# Indicates how table and database names are stored on disk and used in MySQL.
# Value = 0: Table and database names are stored on disk using the lettercase specified in the
#            CREATE TABLE or CREATE DATABASE statement. Name comparisons are case sensitive.
#            You should not set this variable to 0 if you are running MySQL on a system that has
#            case-insensitive file names (such as Windows or macOS).
# Value = 1: Table names are stored in lowercase on disk and name comparisons are not
#            case-sensitive. MySQL converts all table names to lowercase on storage and lookup.
#            This behavior also applies to database names and table aliases.
# Value = 3, Table and database names are stored on disk using the lettercase specified in the
#            CREATE TABLE or CREATE DATABASE statement, but MySQL converts them to lowercase on
#            lookup. Name comparisons are not case sensitive. This works only on file systems
#            that are not case-sensitive! InnoDB table names and view names are stored in
#            lowercase, as for Value = 1.
# NOTE: lower_case_table_names can only be configured when initializing the server.
#       Changing the lower_case_table_names setting after the server is initialized is prohibited.
lower_case_table_names=1

# Secure File Priv.
secure-file-priv="C:/ProgramData/MySQL/MySQL Server 8.0/Uploads"

# The maximum amount of concurrent sessions the MySQL server will
# allow. One of these connections will be reserved for a user with
# SUPER privileges to allow the administrator to login even if the
# connection limit has been reached.
max_connections=151

# The number of open tables for all threads. Increasing this value
# increases the number of file descriptors that mysqld requires.
# Therefore you have to make sure to set the amount of open files
# allowed to at least 4096 in the variable "open-files-limit" in
# section [mysqld_safe]
table_open_cache=2000

# Maximum size for internal (in-memory) temporary tables. If a table
# grows larger than this value, it is automatically converted to disk
# based table This limitation is for a single table. There can be many
# of them.
tmp_table_size=109M

# How many threads we should keep in a cache for reuse. When a client
# disconnects, the client's threads are put in the cache if there aren't
# more than thread_cache_size threads from before.  This greatly reduces
# the amount of thread creations needed if you have a lot of new
# connections. (Normally this doesn't give a notable performance
# improvement if you have a good thread implementation.)
thread_cache_size=10

#*** MyISAM Specific options
# The maximum size of the temporary file MySQL is allowed to use while
# recreating the index (during REPAIR, ALTER TABLE or LOAD DATA INFILE.
# If the file-size would be bigger than this, the index will be created
# through the key cache (which is slower).
myisam_max_sort_file_size=100G

# If the temporary file used for fast index creation would be bigger
# than using the key cache by the amount specified here, then prefer the
# key cache method.  This is mainly used to force long character keys in
# large tables to use the slower key cache method to create the index.
myisam_sort_buffer_size=208M

# Size of the Key Buffer, used to cache index blocks for MyISAM tables.
# Do not set it larger than 30% of your available memory, as some memory
# is also required by the OS to cache rows. Even if you're not using
# MyISAM tables, you should still set it to 8-64M as it will also be
# used for internal temporary disk tables.
key_buffer_size=8M

# Size of the buffer used for doing full table scans of MyISAM tables.
# Allocated per thread, if a full scan is needed.
read_buffer_size=64K

read_rnd_buffer_size=256K

#*** INNODB Specific options ***
# innodb_data_home_dir=

# Use this option if you have a MySQL server with InnoDB support enabled
# but you do not plan to use it. This will save memory and disk space
# and speed up some things.
# skip-innodb

# If set to 1, InnoDB will flush (fsync) the transaction logs to the
# disk at each commit, which offers full ACID behavior. If you are
# willing to compromise this safety, and you are running small
# transactions, you may set this to 0 or 2 to reduce disk I/O to the
# logs. Value 0 means that the log is only written to the log file and
# the log file flushed to disk approximately once per second. Value 2
# means the log is written to the log file at each commit, but the log
# file is only flushed to disk approximately once per second.
innodb_flush_log_at_trx_commit=1

# The size of the buffer InnoDB uses for buffering log data. As soon as
# it is full, InnoDB will have to flush it to disk. As it is flushed
# once per second anyway, it does not make sense to have it very large
# (even with long transactions).
innodb_log_buffer_size=1M

# InnoDB, unlike MyISAM, uses a buffer pool to cache both indexes and
# row data. The bigger you set this the less disk I/O is needed to
# access data in tables. On a dedicated database server you may set this
# parameter up to 80% of the machine physical memory size. Do not set it
# too large, though, because competition of the physical memory may
# cause paging in the operating system.  Note that on 32bit systems you
# might be limited to 2-3.5G of user level memory per process, so do not
# set it too high.
innodb_buffer_pool_size=8M

# Size of each log file in a log group. You should set the combined size
# of log files to about 25%-100% of your buffer pool size to avoid
# unneeded buffer pool flush activity on log file overwrite. However,
# note that a larger logfile size will increase the time needed for the
# recovery process.
innodb_log_file_size=48M

# Number of threads allowed inside the InnoDB kernel. The optimal value
# depends highly on the application, hardware as well as the OS
# scheduler properties. A too high value may lead to thread thrashing.
innodb_thread_concurrency=25

# The increment size (in MB) for extending the size of an auto-extend InnoDB system tablespace file when it becomes full.
innodb_autoextend_increment=64

# The number of regions that the InnoDB buffer pool is divided into.
# For systems with buffer pools in the multi-gigabyte range, dividing the buffer pool into separate instances can improve concurrency,
# by reducing contention as different threads read and write to cached pages.
innodb_buffer_pool_instances=8

# Determines the number of threads that can enter InnoDB concurrently.
innodb_concurrency_tickets=5000

# Specifies how long in milliseconds (ms) a block inserted into the old sublist must stay there after its first access before
# it can be moved to the new sublist.
innodb_old_blocks_time=1000

# It specifies the maximum number of .ibd files that MySQL can keep open at one time. The minimum value is 10.
innodb_open_files=300

# When this variable is enabled, InnoDB updates statistics during metadata statements.
innodb_stats_on_metadata=0

# When innodb_file_per_table is enabled (the default in 5.6.6 and higher), InnoDB stores the data and indexes for each newly created table
# in a separate .ibd file, rather than in the system tablespace.
innodb_file_per_table=1

# Use the following list of values: 0 for crc32, 1 for strict_crc32, 2 for innodb, 3 for strict_innodb, 4 for none, 5 for strict_none.
innodb_checksum_algorithm=0

# The number of outstanding connection requests MySQL can have.
# This option is useful when the main MySQL thread gets many connection requests in a very short time.
# It then takes some time (although very little) for the main thread to check the connection and start a new thread.
# The back_log value indicates how many requests can be stacked during this short time before MySQL momentarily
# stops answering new requests.
# You need to increase this only if you expect a large number of connections in a short period of time.
back_log=80

# If this is set to a nonzero value, all tables are closed every flush_time seconds to free up resources and
# synchronize unflushed data to disk.
# This option is best used only on systems with minimal resources.
flush_time=0

# The minimum size of the buffer that is used for plain index scans, range index scans, and joins that do not use
# indexes and thus perform full table scans.
join_buffer_size=256K

# The maximum size of one packet or any generated or intermediate string, or any parameter sent by the
# mysql_stmt_send_long_data() C API function.
max_allowed_packet=4M

# If more than this many successive connection requests from a host are interrupted without a successful connection,
# the server blocks that host from performing further connections.
max_connect_errors=100

# Changes the number of file descriptors available to mysqld.
# You should try increasing the value of this option if mysqld gives you the error "Too many open files".
open_files_limit=4161

# If you see many sort_merge_passes per second in SHOW GLOBAL STATUS output, you can consider increasing the
# sort_buffer_size value to speed up ORDER BY or GROUP BY operations that cannot be improved with query optimization
# or improved indexing.
sort_buffer_size=256K

# The number of table definitions (from .frm files) that can be stored in the definition cache.
# If you use a large number of tables, you can create a large table definition cache to speed up opening of tables.
# The table definition cache takes less space and does not use file descriptors, unlike the normal table cache.
# The minimum and default values are both 400.
table_definition_cache=1400

# Specify the maximum size of a row-based binary log event, in bytes.
# Rows are grouped into events smaller than this size if possible. The value should be a multiple of 256.
binlog_row_event_max_size=8K

# If the value of this variable is greater than 0, a replication slave synchronizes its master.info file to disk.
# (using fdatasync()) after every sync_master_info events.
sync_master_info=10000

# If the value of this variable is greater than 0, the MySQL server synchronizes its relay log to disk.
# (using fdatasync()) after every sync_relay_log writes to the relay log.
sync_relay_log=10000

# If the value of this variable is greater than 0, a replication slave synchronizes its relay-log.info file to disk.
# (using fdatasync()) after every sync_relay_log_info transactions.
sync_relay_log_info=10000

# Load mysql plugins at start."plugin_x ; plugin_y".
# plugin_load

# The TCP/IP Port the MySQL Server X Protocol will listen on.
loose_mysqlx_port=33060
```

###  （二）执行命令

管理员的模式打开cmd,进入mysql-8-master内的bin 目录下执行：

1. 安装注册服务： `mysqld -insall MySQL-Master`

>  安装完mysql服务后可以在任务管理器中看到该服务，启动该服务时如果报服务名错误或者找不到需要检查服务名是否正确。

<img src="https://img-blog.csdnimg.cn/b18b507c01d4416b8330dc53b758aabe.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBATHZRaUZlbg==,size_20,color_FFFFFF,t_70,g_se,x_16" alt="在这里插入图片描述" style="zoom: 67%;" />

2. 初始化data数据：`mysqld --initialize --user=mysql --console`

> 初始化data数据的时候  `mysqld --initialize --user=mysql --console` 该命令会打印出生成的初始数据库临时密码，需要记下方便登录，如果不小心忘记了，删掉data文件夹，重新执行下该命令会重新生成data文件夹和相关数据文件

3. 启动mysql服务：`net satrt mysql-master`
4. 连接mysql服务器：`mysql -u root -p`
5. 修改密码：`ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '123456';`

执行过程中如有报错，查看错误集合内是否有对应的案例。

### （一）演示图例

<img src="https://img-blog.csdnimg.cn/b85069451e4642189ab98434377c9cb2.png" alt="在这里插入图片描述" style="zoom:80%;" />

<img src="https://img-blog.csdnimg.cn/a4ff8b1da94d44bcaecf71c67d4ea6ca.png" alt="在这里插入图片描述" style="zoom:80%;" />

<img src="https://img-blog.csdnimg.cn/0401ff50412a48d094ecf45722001105.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBATHZRaUZlbg==,size_20,color_FFFFFF,t_70,g_se,x_16" alt="在这里插入图片描述" style="zoom:80%;" />

## 三、 从服务配置

> 从服务一样分别手动创建my.ini文件，输入参数。从mysql服务的安装和初始化参考主mysql服务的安装和初始化步骤这里不再重复。

### （一）mysql-8-slave1 的 my.ini文件

> 要自己在 mysql-8-slave1 里的 bin目录的同一层级 自己手动创建my.ini文件。核心参数：如下参考。**注意 port,server-id,basedir,datadir 的值不和 mysql-8-master,的my.ini 内容重复。**

```ini
[mysqld]
#设置服务端端口
port=33062
#MySQL安装路径
basedir=D:\mysql\mysql-8-slave1
#MySQL数据存放路径
datadir=D:\mysql\mysql-8-slave1\data
#连接到服务器时使用的默认认证插件
default_authentication_plugin=caching_sha2_password
#当创建新表时将使用的默认存储引擎  
default-storage-engine=INNODB
#二进制binglog Binary Logging.
log-bin="mysql-slave1-bin"
# 服务ID
server-id=2
# Set the SQL mode to strict
sql-mode="STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION"
```

### （二）mysql-8-slave2 的 my.ini文件

> 要自己在 mysql-8-slave2 里的 bin目录的同一层级 自己手动创建my.ini文件。核心参数：如下参考。**注意 port,server-id,basedir,datadir 的值 不和 mysql-8-master,mysql-8-slave1的my.ini 内容重复。**

```ini
[mysqld]
#设置服务端端口
port=33063
#MySQL安装路径
basedir=D:\mysql\mysql-8-slave2
#MySQL数据存放路径
datadir=D:\mysql\mysql-8-slave2\data
#连接到服务器时使用的默认认证插件
default_authentication_plugin=caching_sha2_password
#当创建新表时将使用的默认存储引擎  
default-storage-engine=INNODB
#二进制binglog Binary Logging.
log-bin="mysql-slave2-bin"
# 服务ID
server-id=3
# Set the SQL mode to strict
sql-mode="STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION"
```

## 四、主从关联配置

> 主服务需要提供一个账号给从服务建立连接复制binlog日志。这里给主mysql服务创建一个 名为 master的账号，密码 123456  。从服务需要自己加到主服务的从节点中，注意从服务执行`show slave status \G`查看slave状态时 的几个核心参数：
>
> ​            Slave_IO_Running: Yes
> ​            Slave_SQL_Running: Yes
>
> ​              Last_IO_Errno: 0
> ​              Last_IO_Error:
> ​              Last_SQL_Errno: 0
> ​              Last_SQL_Error:
>
> **Slave_IO_Running 和 Slave_SQL_Running 都为yes是正常的。为其它值都是不成功的。此时需要关注 Last_SQL_Error，Last_IO_Error的报错提示，处理异常。**



相关命令参考：

- 查看master状态: `show master status;`
- 查看slave的状态:` show slave status;`
- 开启主从同步：`start slave;`
- 关闭主从同步：`stop slave;`
- 查看提示信息：`show warnings;`
-  查看 slave 的状态（注意：\G后面没有分号的） ：`show slave status \G`
-  `show binlog events;`   #只查看第一个binlog文件的内容
- `show binlog events in 'mysql-bin.000002';`#查看指定binlog文件的内容
- `show binary logs;`  #获取binlog文件列表
- `show master status;` #查看当前正在写入的binlog文件
- `set SQL_LOG_BIN=0;` #在主服务执行暂时关闭binlog = 1时恢复。
- `show variables like '%binlog_format%';`#查看当前binlog写入的版式。
- `show variables like '%port%';`#查看当前mysql服务使用的端口。

```bash
# MASTER_HOST：主数据库的主机ip
# MASTER_PORT：主数据库的端口，不设置则默认是3306
# MASTER_USER：主数据库被授予同步复制权限的用户名
# MASTER_PASSWORD：对应的用户密码
# MASTER_LOG_FILE：在主数据库执行命令show master status 查询到的二进制日志文件名称
# MASTER_LOG_POS：在主数据库执行命令show master status 查询到的位置 Position的值
CHANGE MASTER TO
    MASTER_HOST='localhost',  
    MASTER_PORT = 33061,
    MASTER_USER='master',      
    MASTER_PASSWORD='123456',   
    MASTER_LOG_FILE='mysql-master-bin.000001',
    MASTER_LOG_POS=2414;
```

### （一）主服务

```sql
mysql> show master status;
+-------------------------+----------+--------------+------------------+-------------------+
| File                    | Position | Binlog_Do_DB | Binlog_Ignore_DB | Executed_Gtid_Set |
+-------------------------+----------+--------------+------------------+-------------------+
| mysql-master-bin.000001 |      447 |              |                  |                   |
+-------------------------+----------+--------------+------------------+-------------------+
1 row in set (0.16 sec)

mysql> create user 'master'@'%' identified with mysql_native_password by '123456';
Query OK, 0 rows affected (0.03 sec)
mysql> grant replication slave on *.* to 'master'@'%';
Query OK, 0 rows affected (0.01 sec)

mysql> flush privileges
    -> ;
Query OK, 0 rows affected (0.01 sec)

mysql> show master status;
+-------------------------+----------+--------------+------------------+-------------------+
| File                    | Position | Binlog_Do_DB | Binlog_Ignore_DB | Executed_Gtid_Set |
+-------------------------+----------+--------------+------------------+-------------------+
| mysql-master-bin.000001 |     1151 |              |                  |                   |
+-------------------------+----------+--------------+------------------+-------------------+
1 row in set (0.02 sec)

mysql> show schemas;
+--------------------+
| Database           |
+--------------------+
| information_schema |
| mysql              |
| performance_schema |
| sys                |
+--------------------+
4 rows in set (0.03 sec)

mysql> create schema db;
Query OK, 1 row affected (0.01 sec)

mysql> show schemas;
+--------------------+
| Database           |
+--------------------+
| db                 |
| information_schema |
| mysql              |
| performance_schema |
| sys                |
+--------------------+
5 rows in set (0.03 sec)

mysql> show master status;
+-------------------------+----------+--------------+------------------+-------------------+
| File                    | Position | Binlog_Do_DB | Binlog_Ignore_DB | Executed_Gtid_Set |
+-------------------------+----------+--------------+------------------+-------------------+
| mysql-master-bin.000001 |     1328 |              |                  |                   |
+-------------------------+----------+--------------+------------------+-------------------+
1 row in set (0.02 sec)

mysql> 
```

### （二）从服务slave1

> 配置读取主服务的binlog二进制文件

```mysql
mysql> show slave status;
Empty set

mysql> CHANGE MASTER TO
    MASTER_HOST='localhost',  
    MASTER_PORT = 33061,
    MASTER_USER='master',      
    MASTER_PASSWORD='123456',   
    MASTER_LOG_FILE='mysql-master-bin.000001',
    MASTER_LOG_POS=1328;
Query OK, 0 rows affected (0.06 sec)

mysql> start slave;
Query OK, 0 rows affected, 1 warning (0.38 sec)

mysql> show slave status\G
*************************** 1. row ***************************
               Slave_IO_State: Waiting for source to send event
                  Master_Host: localhost
                  Master_User: master
                  Master_Port: 33061
                Connect_Retry: 60
              Master_Log_File: mysql-master-bin.000001
          Read_Master_Log_Pos: 3221
               Relay_Log_File: DESKTOP-40R603D-relay-bin.000003
                Relay_Log_Pos: 898
        Relay_Master_Log_File: mysql-master-bin.000001
             Slave_IO_Running: Yes
            Slave_SQL_Running: Yes
              Replicate_Do_DB:
          Replicate_Ignore_DB:
           Replicate_Do_Table:
       Replicate_Ignore_Table:
      Replicate_Wild_Do_Table:
  Replicate_Wild_Ignore_Table:
                   Last_Errno: 0
                   Last_Error:
                 Skip_Counter: 0
          Exec_Master_Log_Pos: 3221
              Relay_Log_Space: 1756
              Until_Condition: None
               Until_Log_File:
                Until_Log_Pos: 0
           Master_SSL_Allowed: No
           Master_SSL_CA_File:
           Master_SSL_CA_Path:
              Master_SSL_Cert:
            Master_SSL_Cipher:
               Master_SSL_Key:
        Seconds_Behind_Master: 0
Master_SSL_Verify_Server_Cert: No
                Last_IO_Errno: 0
                Last_IO_Error:
               Last_SQL_Errno: 0
               Last_SQL_Error:
  Replicate_Ignore_Server_Ids:
             Master_Server_Id: 1
                  Master_UUID: 8f3a5eb3-bba1-11ec-adee-00e04c8549f2
             Master_Info_File: mysql.slave_master_info
                    SQL_Delay: 0
          SQL_Remaining_Delay: NULL
      Slave_SQL_Running_State: Replica has read all relay log; waiting for more updates
           Master_Retry_Count: 86400
                  Master_Bind:
      Last_IO_Error_Timestamp:
     Last_SQL_Error_Timestamp:
               Master_SSL_Crl:
           Master_SSL_Crlpath:
           Retrieved_Gtid_Set:
            Executed_Gtid_Set:
                Auto_Position: 0
         Replicate_Rewrite_DB:
                 Channel_Name:
           Master_TLS_Version:
       Master_public_key_path:
        Get_master_public_key: 0
            Network_Namespace:
1 row in set, 1 warning (0.10 sec)

```

### （三）从服务slave2

> 配置读取主服务的binlog二进制文件

```mysql
mysql> CHANGE MASTER TO
    ->     MASTER_HOST='localhost',
    ->     MASTER_PORT = 33061,
    ->     MASTER_USER='master',
    ->     MASTER_PASSWORD='123456',
    ->     MASTER_LOG_FILE='mysql-master-bin.000001',
    ->     MASTER_LOG_POS=2414;
Query OK, 0 rows affected, 8 warnings (0.23 sec)

mysql> start slave;
Query OK, 0 rows affected, 1 warning (0.38 sec)

mysql> show slave status \G
*************************** 1. row ***************************
               Slave_IO_State: Waiting for source to send event
                  Master_Host: localhost
                  Master_User: master
                  Master_Port: 33061
                Connect_Retry: 60
              Master_Log_File: mysql-master-bin.000001
          Read_Master_Log_Pos: 3221
               Relay_Log_File: DESKTOP-40R603D-relay-bin.000002
                Relay_Log_Pos: 1140
        Relay_Master_Log_File: mysql-master-bin.000001
             Slave_IO_Running: Yes
            Slave_SQL_Running: Yes
              Replicate_Do_DB:
          Replicate_Ignore_DB:
           Replicate_Do_Table:
       Replicate_Ignore_Table:
      Replicate_Wild_Do_Table:
  Replicate_Wild_Ignore_Table:
                   Last_Errno: 0
                   Last_Error:
                 Skip_Counter: 0
          Exec_Master_Log_Pos: 3221
              Relay_Log_Space: 1360
              Until_Condition: None
               Until_Log_File:
                Until_Log_Pos: 0
           Master_SSL_Allowed: No
           Master_SSL_CA_File:
           Master_SSL_CA_Path:
              Master_SSL_Cert:
            Master_SSL_Cipher:
               Master_SSL_Key:
        Seconds_Behind_Master: 0
Master_SSL_Verify_Server_Cert: No
                Last_IO_Errno: 0
                Last_IO_Error:
               Last_SQL_Errno: 0
               Last_SQL_Error:
  Replicate_Ignore_Server_Ids:
             Master_Server_Id: 1
                  Master_UUID: 8f3a5eb3-bba1-11ec-adee-00e04c8549f2
             Master_Info_File: mysql.slave_master_info
                    SQL_Delay: 0
          SQL_Remaining_Delay: NULL
      Slave_SQL_Running_State: Replica has read all relay log; waiting for more updates
           Master_Retry_Count: 86400
                  Master_Bind:
      Last_IO_Error_Timestamp:
     Last_SQL_Error_Timestamp:
               Master_SSL_Crl:
           Master_SSL_Crlpath:
           Retrieved_Gtid_Set:
            Executed_Gtid_Set:
                Auto_Position: 0
         Replicate_Rewrite_DB:
                 Channel_Name:
           Master_TLS_Version:
       Master_public_key_path:
        Get_master_public_key: 0
            Network_Namespace:
1 row in set, 1 warning (0.01 sec)

mysql>
```

## 五、错误合集

### （一）服务名无效、服务无法启动

> 未安装,服务名输入错误，或者未注册服务会报如下错误

![在这里插入图片描述](https://img-blog.csdnimg.cn/ae2cc1c45ad14e8ca180da05be915f3b.png)

> 常见有：my.ini 文件缺失，或者 my.ini内的参数不正确例如datadir的目录是手动创建的没有通过初始化命令自动生成。

![在这里插入图片描述](https://img-blog.csdnimg.cn/67efdafe910d445c8ef86f37f1f94f8f.png)

### （二）Last_IO_Error：Authentication plugin 'caching_sha2_password' reported error

> 主服务的用于提供给从服务连接读取binlog的用户密码设置时的加密规则和默认规则不一样，需要进行更改处理

```sql
Last_IO_Error: error connecting to master 'master@localhost:33061' - retry-time: 60 retries: 7 message: Authentication plugin 'caching_sha2_password' reported error: Authentication requires secure connection.
```

![在这里插入图片描述](https://img-blog.csdnimg.cn/90082c8510d44ab39c9ceaca4e60a533.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBATHZRaUZlbg==,size_20,color_FFFFFF,t_70,g_se,x_16)

解决参考: [Mysql8.0.17修改root密码加密方式修改普通用户的加密方式](https://blog.csdn.net/numberseven7/article/details/99548745)

## 参考资料

- [MySQL8.0主从复制的配置](https://blog.csdn.net/u013068184/article/details/107691389)
- [MySQL主从配置详解](https://www.jianshu.com/p/b0cf461451fb)
- [Windows环境下MySQL主从配置](https://www.cnblogs.com/opsprobe/p/10904377.html)
- [Mysql8修改root密码加密方式以及修改普通用户的加密](https://blog.csdn.net/numberseven7/article/details/99548745)
- [MySQL服务无法启动原因及解决](https://blog.csdn.net/weixin_44698389/article/details/105363480)

