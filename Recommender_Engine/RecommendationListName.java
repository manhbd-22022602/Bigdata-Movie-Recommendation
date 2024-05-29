import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.chain.ChainMapper;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;

public class RecommendationListName {
    public static class RecommendationListMapper extends Mapper<LongWritable, Text, Text, Text> {

		// MAP METHOD
		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			//input: user \t movie:recommend_score
			//pass data to reducer

			String[] line = value.toString().split("\t");
            String user = line[0];
            String movie = line[1].split(":")[0];
            String score = line[1].split(":")[1];
			context.write(new Text(movie), new Text(user + "=" + movie + "=" + score));
		}
	}

	public static class TitlesMapper extends Mapper<LongWritable, Text, Text, Text> {

		// MAP METHOD
		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

			//input: movie,name
			//pass data to reducer
			String[] line = value.toString().split(",");
            String movie = line[0];
            String name = line[1];
			context.write(new Text(movie), new Text(movie + ":" + name));

		}
	}

	public static class RecommendationListNameReducer extends Reducer<Text, Text, Text, Text> {
		// REDUCE METHOD
		@Override
		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {

			//key = movie value = <userA=score, userB=score... movie1:name1, movie2:name2...>
			//collect the data for each movie, then replace movie by its name
            ArrayList<ArrayList<String>> outerList = new ArrayList<ArrayList<String>>();
            Map<String, String> movieMap = new HashMap<String, String>();

			for (Text value:values){
				if (value.toString().contains("=")){
					String[] user_movie_score = value.toString().split("=");
                    ArrayList<String> innerList = new ArrayList<String>();
                    String user = user_movie_score[0];
                    String movie = user_movie_score[1];
                    String score = user_movie_score[2];
                    innerList.add(user);
                    innerList.add(movie);
                    innerList.add(score);
                    outerList.add(innerList);
				}
				else{
					String[] movie_name = value.toString().split(":");
                    String movie = movie_name[0];
                    String name = movie_name[1];
                    movieMap.put(movie, name);
				}
			}

            for (ArrayList<String> innerList:outerList){
                String user = innerList.get(0);
                String movie = innerList.get(1);
                String score = innerList.get(2);
                context.write(new Text(user), new Text(movieMap.get(movie) + ":" + score));
            }
		}
	}


	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();

		Job job = Job.getInstance(conf);
		job.setJarByClass(RecommendationListName.class);

		ChainMapper.addMapper(job, RecommendationListMapper.class, LongWritable.class, Text.class, Text.class, Text.class, conf);
		ChainMapper.addMapper(job, TitlesMapper.class, Text.class, Text.class, Text.class, Text.class, conf);

		job.setMapperClass(RecommendationListMapper.class);
		job.setMapperClass(TitlesMapper.class);

		job.setReducerClass(RecommendationListNameReducer.class);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		MultipleInputs.addInputPath(job, new Path(args[0]), TextInputFormat.class, RecommendationListMapper.class);
		MultipleInputs.addInputPath(job, new Path(args[1]), TextInputFormat.class, TitlesMapper.class);

		TextOutputFormat.setOutputPath(job, new Path(args[2]));
		
		job.waitForCompletion(true);
	}
}
