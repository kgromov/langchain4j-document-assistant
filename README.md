# OpenAI Chat Vaadin App
___
This is a small sample how to use OpenAI chat completion API in Vaadin and     
Spring Boot to create an own version of ChatGPT app using the Vaadin flow. 
Application allows to upload files for own knowledge base and ask specific questions.   
Supported file extensions are: *.pdf, .xls, xlsx, .csv and .txt*   
As embedding store can be used any vector search DB (in this example astraDb) or in memory solution.   
_________
For integration with OpenAI ``langchain4j`` library is used with appropriate ``langchain4j-document-parser`` extensions.    
To use inmemory store just setup ``settings.openai.api-key`` or environment variable with your OpenAI API key.   
_________
Example of chat looks like this:
![Screenshot 2024-02-07 002444](https://github.com/kgromov/langchain4j-document-assistant/assets/9352794/5373bef5-c871-4850-b0aa-cbb551d3e2a1)
