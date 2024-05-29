
public class Driver {
	public static void main(String[] args) throws Exception {
		
		DataDividerByUser dataDividerByUser = new DataDividerByUser();
		CoOccurrenceMatrixGenerator coOccurrenceMatrixGenerator = new CoOccurrenceMatrixGenerator();
		Normalize normalize = new Normalize();
		Multiplication multiplication = new Multiplication();
		Sum sum = new Sum();
		RecommendationListGenerator generator = new RecommendationListGenerator();
		RecommendationListName names = new RecommendationListName();
		RecommendationName recommendationName = new RecommendationName();

		String rawInput = args[0];
		String Titles = args[1];
		String userMovieListOutputDir = args[2];
		String coOccurrenceMatrixDir = args[3];
		String normalizeDir = args[4];
		String multiplicationDir = args[5];
		String sumDir = args[6];
		String recommendationListDir = args[7];
		String recommendationListName = args[8];
		String recommendationList = args[9];
		String[] path1 = {rawInput, userMovieListOutputDir};
		String[] path2 = {userMovieListOutputDir, coOccurrenceMatrixDir};
		String[] path3 = {coOccurrenceMatrixDir, normalizeDir};
		String[] path4 = {normalizeDir, rawInput, multiplicationDir};
		String[] path5 = {multiplicationDir, sumDir};
		String[] path6 = {sumDir, recommendationListDir};
		String[] path7 = {recommendationListDir, Titles, recommendationListName};
		String[] path8 = {recommendationListName, recommendationList};
		
		dataDividerByUser.main(path1);
		coOccurrenceMatrixGenerator.main(path2);
		normalize.main(path3);
		multiplication.main(path4);
		sum.main(path5);
		generator.main(path6);
		names.main(path7);
		recommendationName.main(path8);
	}

}
