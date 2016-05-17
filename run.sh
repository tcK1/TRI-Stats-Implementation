javac -d ./bin ./src/Main.java
read -p "Quantidade de iterações: " QTD_IT
java -cp ./bin Main $QTD_IT
