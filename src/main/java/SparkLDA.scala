import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import scala.collection.mutable._
import org.apache.commons.compress.archivers.zip.AsiExtraField



object SparkLDA {

	def main(args: Array[String]) {
		importText("hdfs://localhost:8020/README.md",10);
	}

	def lda(pathToFileIn:String,pathToFileOut:String,pathEvalLabels:String,numTopics:Int,alpha:Double,beta:Double,numIter:Int,deBug:Boolean){

	}
	def initializeSpark{
		val conf = new SparkConf().setAppName("Simple Application")
				val sc = new SparkContext(conf)
		(conf,sc)
	}

	def importText(pathToFileIn:String,numTopics:Int){//,sc:SparkContext){
		val conf = new SparkConf().setAppName("Simple Application")
				val sc = new SparkContext(conf)
		val stopWords =List[String]("a","able","about","above","according","accordingly","across","actually","after","afterwards","again","against","all","allow","allows","almost","alone","along","already","also","although","always","am","among","amongst","an","and","another","any","anybody","anyhow","anyone","anything","anyway","anyways","anywhere","apart","appear","appreciate","appropriate","are","around","as","aside","ask","asking","associated","at","available","away","awfully","b","be","became","because","become","becomes","becoming","been","before","beforehand","behind","being","believe","below","beside","besides","best","better","between","beyond","both","brief","but","by","c","came","can","cannot","cant","cause","causes","certain","certainly","changes","clearly","co","com","come","comes","concerning","consequently","consider","considering","contain","containing","contains","corresponding","could","course","currently","d","definitely","described","despite","did","different","do","does","doing","done","down","downwards","during","e","each","edu","eg","eight","either","else","elsewhere","enough","entirely","especially","et","etc","even","ever","every","everybody","everyone","everything","everywhere","ex","exactly","example","except","f","far","few","fifth","first","five","followed","following","follows","for","former","formerly","forth","four","from","further","furthermore","g","get","gets","getting","given","gives","go","goes","going","gone","got","gotten","greetings","h","had","happens","hardly","has","have","having","he","hello","help","hence","her","here","hereafter","hereby","herein","hereupon","hers","herself","hi","him","himself","his","hither","hopefully","how","howbeit","however","i","ie","if","ignored","immediate","in","inasmuch","inc","indeed","indicate","indicated","indicates","inner","insofar","instead","into","inward","is","it","its","itself","j","just","k","keep","keeps","kept","know","knows","known","l","last","lately","later","latter","latterly","least","less","lest","let","like","liked","likely","little","ll","look","looking","looks","ltd","m","mainly","many","may","maybe","me","mean","meanwhile","merely","might","more","moreover","most","mostly","much","must","my","myself","n","name","namely","nd","near","nearly","necessary","need","needs","neither","never","nevertheless","new","next","nine","no","nobody","non","none","noone","nor","normally","not","nothing","novel","now","nowhere","o","obviously","of","off","often","oh","ok","okay","old","on","once","one","ones","only","onto","or","other","others","otherwise","ought","our","ours","ourselves","out","outside","over","overall","own","p","particular","particularly","per","perhaps","placed","please","plus","possible","presumably","probably","provides","q","que","quite","qv","r","rather","rd","re","really","reasonably","regarding","regardless","regards","relatively","respectively","right","s","said","same","saw","say","saying","says","second","secondly","see","seeing","seem","seemed","seeming","seems","seen","self","selves","sensible","sent","serious","seriously","seven","several","shall","she","should","since","six","so","some","somebody","somehow","someone","something","sometime","sometimes","somewhat","somewhere","soon","sorry","specified","specify","specifying","still","sub","such","sup","sure","t","take","taken","tell","tends","th","than","thank","thanks","thanx","that","thats","the","their","theirs","them","themselves","then","thence","there","thereafter","thereby","therefore","therein","theres","thereupon","these","they","think","third","this","thorough","thoroughly","those","though","three","through","throughout","thru","thus","to","together","too","took","toward","towards","tried","tries","truly","try","trying","twice","two","u","un","under","unfortunately","unless","unlikely","until","unto","up","upon","us","use","used","useful","uses","using","usually","uucp","v","value","various","ve","very","via","viz","vs","w","want","wants","was","way","we","welcome","well","went","were","what","whatever","when","whence","whenever","where","whereafter","whereas","whereby","wherein","whereupon","wherever","whether","which","while","whither","who","whoever","whole","whom","whose","why","will","willing","wish","with","within","without","wonder","would","would","x","y","yes","yet","you","your","yours","yourself","yourselves","z","zero");
		val textFile=sc.textFile(pathToFileIn);
		var index=0;
		var dictionnary:scala.collection.mutable.Map[String,Int] = scala.collection.mutable.Map[String,Int]();

		var wordToTopics:scala.collection.mutable.Map[String, Array[Int]] = new HashMap[String, Array[Int]]();

		//var topMat = Array.ofDim[Int](2,2)
		//topMat(0) = Array(1,2)
		var topMat:scala.collection.mutable.ArrayBuffer[Array[Int]] = new scala.collection.mutable.ArrayBuffer[Array[Int]]()


				//		val documents=textFile.map(line=>{
				//			index=index+1;
				//      println("in")
				//			(index,line.split(" ").map(word => {
				//          
				//				if(!stopWords.contains(word)){
				//					
				//					var num=dictionnary.getOrElse(word,0)
				//          
				//					num=num+1;
				//					dictionnary+=(word -> num);
				//					(word, 1);
				//				}
				//			}))
				//		});



				val documents=textFile.map(line=>{
					index=index+1;
					val lineCleaned=line.replaceAll("[^A-Za-z ]","").toLowerCase();
					(index,lineCleaned.split(" ").map(word=>{
						if(word.length()>1&&(!stopWords.contains(word))){
							var randNum = new scala.util.Random
									(word,randNum.nextInt(numTopics-1))
						}
					}))
				})



				/*
    for (doc <- documents) {

      val tup3 = doc._2

      for (tup2 <- tup3) {



          tup2 match {
          case (word, topic) => {

            //grab the topic count array corresponding to the word, get the topic number, and pass increment the corresponding topic count
            val currentArray:Array[Int] = wordToTopics.getOrElse(word.toString(),new Array[Int](numTopics));
            currentArray(topic.asInstanceOf[Int])+=1;
            wordToTopics.put(word.toString(), currentArray)

            //print the current array

            for ( x <- 0 to numTopics-1 ) {
              print(currentArray(x) + " ")
            }
            println()


            topMat.append(currentArray)

          }
          case _ =>

          }
      }

    }
				 */

				documents.collect.foreach(t=>t._2.foreach{f=> {
					f match{
					case (word, topic) => {

						//grab the topic count array corresponding to the word, get the topic number, and pass increment the corresponding topic count
						val currentArray:Array[Int] = wordToTopics.getOrElse(word.toString(),new Array[Int](numTopics));
					  currentArray(topic.asInstanceOf[Int])+=1;
					  wordToTopics.put(word.toString(), currentArray)
            
            //add to dictionary
            dictionnary.put(word.toString(),0);

					}
					case _ =>

					}
				}
				}
		    );

    println("size of dictionary :"+ dictionnary.size+" size file :"+textFile.count())
		println("size of wordToTopics :"+ wordToTopics.size+" size file :"+textFile.count())
		
    //print out the map
    for ( w <- wordToTopics ) {
      val (key, topCount) = w
      for (x <- 0 to topCount.length-1) {
          print(topCount(x) + " ")
      }
      println(", word = " + key.toString())
    }
    println()
    
    //print out the dictionary
    for ( d <- dictionnary ) {
      val (key, topCount) = d
      println("word = " + key.toString())
    }
    println()
    
    (documents,dictionnary)

		//val test:Array[Int] = topMat(0)
		//println(test(0) + " " + test(1))


		//val temp:scala.collection.mutable.LinkedList[Int] = wordToTopics.get(t._1)
		//Map.put(t._1, )

	}
	//	def initializeTopicDistribution(documents:RDD[String]){
	//		/*
	//		 * creates a document RDD with for each line each word and a random topic ex: (obama,1)(obama,2)(table,1) ...
	//		 * for each line in the documents, for each word in the line, for the number of time the word appears, pick a random topic
	//		 * build a string 
	//		 */
	//	}

	def printMetrix(){

	}

	def saveResult{

	}

}