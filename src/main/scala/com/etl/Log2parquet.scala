package com.etl

import com.util.{SchemaUtils, String2Type}
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}


/*进行数据格式转换
* */
object Log2parquet {
  def main(args: Array[String]): Unit = {
    // System.setProperty("hadoop.home.dir", "D:\\Huohu\\下载\\hadoop-common-2.2.0-bin-master")
    // 判断路径是否正确
    if(args.length != 2){
      println("目录参数不正确，退出程序")
      sys.exit()
    }
    // 创建一个集合保存输入和输出目录
    val Array(inputPath,outputPath) = args
    val conf = new SparkConf().setAppName(this.getClass.getName).setMaster("local[*]")
      // 设置序列化方式 采用Kyro序列化方式，比默认序列化方式性能高
      .set("spark.serializer","org.apache.spark.serializer.KryoSerializer")
    // 创建执行入口
    val sc = new SparkContext(conf)
    val sQLContext = new SQLContext(sc)
    // 设置压缩方式 使用Snappy方式进行压缩
    sQLContext.setConf("spark.sql.parquet.compression.codec","snappy")
    // 进行数据的读取，处理分析数据
    val lines = sc.textFile(inputPath)
    // 按要求切割，并且保证数据的长度大于等于85个字段，
    // 如果切割的时候遇到相同切割条件重复的情况下，需要切割的话，那么后面需要加上对应匹配参数
    // 这样切割才会准确 比如 ,,,,,,, 会当成一个字符切割 需要加上对应的匹配参数
    val rowRDD = lines.map(t=>t.split(",",t.length)).filter(_.length >= 85)
      .map(arr=>{
        Row(
          arr(0),
          String2Type.toInt(arr(1)),
          String2Type.toInt(arr(2)),
          String2Type.toInt(arr(3)),
          String2Type.toInt(arr(4)),
          arr(5),
          arr(6),
          String2Type.toInt(arr(7)),
          String2Type.toInt(arr(8)),
          String2Type.toDouble(arr(9)),
          String2Type.toDouble(arr(10)),
          arr(11),
          arr(12),
          arr(13),
          arr(14),
          arr(15),
          arr(16),
          String2Type.toInt(arr(17)),
          arr(18),
          arr(19),
          String2Type.toInt(arr(20)),
          String2Type.toInt(arr(21)),
          arr(22),
          arr(23),
          arr(24),
          arr(25),
          String2Type.toInt(arr(26)),
          arr(27),
          String2Type.toInt(arr(28)),
          arr(29),
          String2Type.toInt(arr(30)),
          String2Type.toInt(arr(31)),
          String2Type.toInt(arr(32)),
          arr(33),
          String2Type.toInt(arr(34)),
          String2Type.toInt(arr(35)),
          String2Type.toInt(arr(36)),
          arr(37),
          String2Type.toInt(arr(38)),
          String2Type.toInt(arr(39)),
          String2Type.toDouble(arr(40)),
          String2Type.toDouble(arr(41)),
          String2Type.toInt(arr(42)),
          arr(43),
          String2Type.toDouble(arr(44)),
          String2Type.toDouble(arr(45)),
          arr(46),
          arr(47),
          arr(48),
          arr(49),
          arr(50),
          arr(51),
          arr(52),
          arr(53),
          arr(54),
          arr(55),
          arr(56),
          String2Type.toInt(arr(57)),
          String2Type.toDouble(arr(58)),
          String2Type.toInt(arr(59)),
          String2Type.toInt(arr(60)),
          arr(61),
          arr(62),
          arr(63),
          arr(64),
          arr(65),
          arr(66),
          arr(67),
          arr(68),
          arr(69),
          arr(70),
          arr(71),
          arr(72),
          String2Type.toInt(arr(73)),
          String2Type.toDouble(arr(74)),
          String2Type.toDouble(arr(75)),
          String2Type.toDouble(arr(76)),
          String2Type.toDouble(arr(77)),
          String2Type.toDouble(arr(78)),
          arr(79),
          arr(80),
          arr(81),
          arr(82),
          arr(83),
          String2Type.toInt(arr(84))
        )
      })
    val df = sQLContext.createDataFrame(rowRDD,SchemaUtils.structtype)
    println("ssss")
    df.write.parquet(outputPath)
    // 关闭
    sc.stop()

  }
}
