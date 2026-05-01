git clone https://github.com/SoakProject/MultiBuild.git

./gradlew build

./MultiBuild/gradlew run -p="./MultiBuild/" --args="./../../Wrapper/src 21"