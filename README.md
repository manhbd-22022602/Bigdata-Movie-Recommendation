# Bigdata-Movie-Recommendation

## Description
Bigdata-Movie-Recommendation là một hệ thống gợi ý phim được xây dựng bằng cách sử dụng Hadoop. Hệ thống này tận dụng các khả năng xử lý phân tán của Hadoop để tính toán và tạo ra danh sách gợi ý phim cho người dùng dựa trên các dữ liệu về hành vi xem phim của họ. 

![Bigdata Architecture](https://github.com/manhbd-22022602/Bigdata-Movie-Recommendation/blob/main/Pictures-Diagrams/System_diagram.png)

Hệ thống bao gồm các bước xử lý dữ liệu sau:
1. **Data Division by User**: Chia dữ liệu thành từng phần theo người dùng.
2. **Co-Occurrence Matrix Calculation**: Tính toán ma trận đồng xuất hiện của các bộ phim.
3. **Normalization**: Chuẩn hóa ma trận đồng xuất hiện.
4. **Multiplication**: Nhân ma trận để tính điểm gợi ý cho từng cặp người dùng - bộ phim.
5. **Summation**: Tổng hợp các điểm gợi ý.
6. **Recommendation List Generation**: Tạo danh sách gợi ý phim theo movie_id cho người dùng.
7. **Recommendation List Name**: Thay thế movie_id bằng tên film trong movie_titles.txt
8. **Recommendation Name**: Sắp xếp Recommendation List Name theo user_id và recommend_score
## How to Run

### Clone the project
```bash
# Clone the project repository
git clone https://github.com/manhbd-22022602/Bigdata-Movie-Recommendation.git
cd Bigdata-Movie-Recommendation

# Ensure your Hadoop cluster is running

# Execute the Recommender Engine
hadoop jar Recommender_Engine/recommender.jar Driver /data_path /data_titles_path /dataDividedByUser /coOccurrenceMatrix /Normalize /Multiplication /Sum /RecommendationList /RecommendationListName /RecommendName
# Copy output file to local
hadoop fs -get / ~/Bigdata-Movie-Recommendation/output
```

 ### Compiling
 ```bash
hadoop com.sun.tools.javac.Main *.java
jar cf recommender.jar *.class
```
