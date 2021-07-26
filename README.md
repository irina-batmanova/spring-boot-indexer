## Run service
1. mvn install
2. java -jar target/indexer-0.0.1-SNAPSHOT.jar

## Endpoints
1. POST /api/v1/files -F file=@/path/to/file - создание файла
2. GET /api/v1/files/{fileId}/download - скачивание файла
3. GET /api/v1/files/{fileId} - получение списка слов в файле
4. GET /api/v1/files/?word=duck - получение списка 
   id файлов, содержащих слово duck 
   (если параметр word не передается - эндпоинт 
   возвращает список всех файлов)
5. DELETE /api/v1/files/{fileId} - удаление файла   
