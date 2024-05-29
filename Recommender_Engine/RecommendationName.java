import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.naming.Context;

public class RecommendationName {
    public static class RecommendationNameMapper extends Mapper<LongWritable, Text, IntWritable, Text> {

		// MAP_Method:divide data by user
		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

			//input user \t movie:score
			String[] user_movie_score = value.toString().trim().split("\t");
			int user = Integer.parseInt(user_movie_score[0]);
            String movie = user_movie_score[1].split(":")[0];
            String score = user_movie_score[1].split(":")[1];

			context.write(new IntWritable(user), new Text(movie + ":" + score));

		}
	}

	public static class RecommendationNameReducer extends Reducer<IntWritable, Text, IntWritable, Text> {
		
		// reduce method

		@Override
		public void reduce(IntWritable key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {

			ArrayList<ArrayList<String>> outerList = new ArrayList<ArrayList<String>>();

            for (Text value : values) {
                String[] movie_score = value.toString().split(":");
                ArrayList<String> innerList = new ArrayList<String>();
                String movie = movie_score[0];
                String score = movie_score[1];
                innerList.add(movie);
                innerList.add(score);
                outerList.add(innerList);
            }

            Collections.sort(outerList, new Comparator<ArrayList<String>>() {
                @Override
                public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                    return Double.compare(Double.parseDouble(o2.get(1)), Double.parseDouble(o1.get(1)));
                }
            });
            for (ArrayList<String> innerList:outerList){
                String movie = innerList.get(0);
                String score = innerList.get(1);
                context.write(key, new Text(movie));
            }
		}
	}

	public static void main(String[] args) throws Exception {

		Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);
		
		job.setMapperClass(RecommendationNameMapper.class);
		job.setReducerClass(RecommendationNameReducer.class);

		job.setJarByClass(RecommendationName.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(Text.class);

		TextInputFormat.setInputPaths(job, new Path(args[0]));
		TextOutputFormat.setOutputPath(job, new Path(args[1]));

		job.waitForCompletion(true);
	}
}
