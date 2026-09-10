#!/bin/bash

# Пути (относительно корня проекта)
SRC_DIR="src/main/java"
OUT_DIR="out"
CLASSES_DIR="$OUT_DIR/classes"
JAR_DIR="$OUT_DIR/jar"
JAVADOC_DIR="$OUT_DIR/javadoc"

# Создаём папки
mkdir -p $CLASSES_DIR $JAR_DIR $JAVADOC_DIR

# Компилируем исходники
javac -d $CLASSES_DIR -sourcepath $SRC_DIR $(find $SRC_DIR -name "*.java")

# Генерируем javadoc
javadoc -d $JAVADOC_DIR -sourcepath $SRC_DIR -subpackages ru.nsu.oop

# Создаём исполняемый JAR
jar cfm $JAR_DIR/sort.jar $SRC_DIR/META-INF/MANIFEST.MF -C $CLASSES_DIR .

# Запускаем приложение
java -jar $JAR_DIR/sort.jar