# Makefile
# サフィックスルールによる依存関係

CC = gcc
TARGET = task13
HEADER = task13.h

SRCS = main13.c task13.c
OBJS = main13.o task13.o

# サフィックスルール
.SUFFIXES: .c .o
.c.o:
	$(CC) -c $<

# デフォルトターゲット
all: $(TARGET)

# 実行ファイル生成
$(TARGET): $(OBJS)
	$(CC) -o $@ $(OBJS)

# 依存関係
main13.o: main13.c $(HEADER)
task13.o: task13.c $(HEADER)

# クリーン
clean:
	rm -f $(OBJS) $(TARGET)

# 実行
run: $(TARGET)
	./$(TARGET)

.PHONY: all clean run
