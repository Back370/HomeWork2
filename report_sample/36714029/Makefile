# Makefile for HomeWork2 Report Sample Programs
# Author: Student 36714029
# Date: 2025-12-28

# =====================================================
# 設定
# =====================================================
JAVAC = javac
JAVA = java
JAVAC_FLAGS = -encoding UTF-8 -d bin -sourcepath .
JAVA_FLAGS = -cp bin

# ディレクトリ
SRC_DIR = .
BIN_DIR = bin

# =====================================================
# デフォルトターゲット
# =====================================================
.PHONY: all
all: compile

# =====================================================
# コンパイル
# =====================================================
.PHONY: compile
compile: $(BIN_DIR)
	@echo "Compiling all Java files..."
	@find . -name "*.java" > .java_files.txt
	@$(JAVAC) $(JAVAC_FLAGS) @.java_files.txt
	@rm .java_files.txt
	@echo "Compilation complete!"

# binディレクトリの作成
$(BIN_DIR):
	@mkdir -p $(BIN_DIR)

# =====================================================
# 個別課題のコンパイル
# =====================================================
.PHONY: ex11a ex11b ex11c ex11d ex12a ex12b ex12c ex12d

ex11a: $(BIN_DIR)
	@echo "Compiling ex11a..."
	@$(JAVAC) $(JAVAC_FLAGS) ex11a/ex1a/Maze.java

ex11b: $(BIN_DIR)
	@echo "Compiling ex11b..."
	@$(JAVAC) $(JAVAC_FLAGS) ex11b/ex1b/*.java

ex11c: $(BIN_DIR)
	@echo "Compiling ex11c..."
	@$(JAVAC) $(JAVAC_FLAGS) ex11c/ex1c/*.java

ex11d: $(BIN_DIR)
	@echo "Compiling ex11d..."
	@$(JAVAC) $(JAVAC_FLAGS) ex11d/ex1d/*.java

ex12a: $(BIN_DIR)
	@echo "Compiling ex12a..."
	@$(JAVAC) $(JAVAC_FLAGS) ex12a/search/*.java ex12a/ex2a/*.java

ex12b: $(BIN_DIR)
	@echo "Compiling ex12b..."
	@$(JAVAC) $(JAVAC_FLAGS) ex12a/search/*.java ex12b/ex2b/*.java

ex12c: $(BIN_DIR)
	@echo "Compiling ex12c..."
	@$(JAVAC) $(JAVAC_FLAGS) ex12a/search/*.java ex12c/ex2c/*.java

ex12d: $(BIN_DIR)
	@echo "Compiling ex12d..."
	@$(JAVAC) $(JAVAC_FLAGS) ex12a/search/*.java ex12d/ex2d/*.java

# =====================================================
# 実行
# =====================================================
.PHONY: run-ex11a run-ex11b run-ex11c run-ex11d
.PHONY: run-ex12a run-ex12b run-ex12c run-ex12d

run-ex11a: ex11a
	@echo "Running ex11a - Maze Problem..."
	@$(JAVA) $(JAVA_FLAGS) ex11a.ex1a.Maze

run-ex11b: ex11b
	@echo "Running ex11b - Maze Problem with Search..."
	@$(JAVA) $(JAVA_FLAGS) ex11b.ex1b.MazeProblem

run-ex11c: ex11c
	@echo "Running ex11c - Missionaries and Cannibals..."
	@$(JAVA) $(JAVA_FLAGS) ex11c.ex1c.MisCanProblem

run-ex11d: ex11d
	@echo "Running ex11d - Missionaries and Cannibals (Advanced)..."
	@$(JAVA) $(JAVA_FLAGS) ex11d.ex1d.MisCanProblem

run-ex12a: ex12a
	@echo "Running ex12a - Missionaries and Cannibals with Heuristics..."
	@$(JAVA) $(JAVA_FLAGS) ex12a.ex2a.Main

run-ex12b: ex12b
	@echo "Running ex12b - Eight Puzzle Problem..."
	@$(JAVA) $(JAVA_FLAGS) ex12b.ex2b.EightPuzzleProblem

run-ex12c: ex12c
	@echo "Running ex12c - Eight Puzzle with Cycle Avoidance..."
	@$(JAVA) $(JAVA_FLAGS) ex12c.ex2c.EightPuzzleProblem

run-ex12d: ex12d
	@echo "Running ex12d - Eight Puzzle with Multiple Heuristics..."
	@$(JAVA) $(JAVA_FLAGS) ex12d.ex2d.EightPuzzleProblem

# =====================================================
# クリーン
# =====================================================
.PHONY: clean
clean:
	@echo "Cleaning compiled files..."
	@rm -rf $(BIN_DIR)
	@rm -f .java_files.txt
	@echo "Clean complete!"

.PHONY: clean-all
clean-all: clean
	@echo "Cleaning all generated files..."
	@find . -name "*.class" -delete
	@find . -name "8-puzzle-ex12d.txt" -delete
	@echo "Deep clean complete!"

# =====================================================
# テスト（全課題実行）
# =====================================================
.PHONY: test
test: compile
	@echo ""
	@echo "========================================"
	@echo "Testing all exercises"
	@echo "========================================"
	@echo ""
	@make run-ex11a
	@echo ""
	@make run-ex11b
	@echo ""
	@make run-ex11c
	@echo ""

# =====================================================
# ヘルプ
# =====================================================
.PHONY: help
help:
	@echo "========================================"
	@echo "HomeWork2 Makefile - 使用方法"
	@echo "========================================"
	@echo ""
	@echo "コンパイル:"
	@echo "  make compile     - 全てのJavaファイルをコンパイル"
	@echo "  make ex11a       - 課題1-1aのみコンパイル"
	@echo "  make ex11b       - 課題1-1bのみコンパイル"
	@echo "  make ex11c       - 課題1-1cのみコンパイル"
	@echo "  make ex11d       - 課題1-1dのみコンパイル"
	@echo "  make ex12a       - 課題1-2aのみコンパイル"
	@echo "  make ex12b       - 課題1-2bのみコンパイル"
	@echo "  make ex12c       - 課題1-2cのみコンパイル"
	@echo "  make ex12d       - 課題1-2dのみコンパイル"
	@echo ""
	@echo "実行:"
	@echo "  make run-ex11a   - 課題1-1aを実行（迷路問題）"
	@echo "  make run-ex11b   - 課題1-1bを実行（迷路探索）"
	@echo "  make run-ex11c   - 課題1-1cを実行（宣教師と人食い人）"
	@echo "  make run-ex11d   - 課題1-1dを実行（繰り返し回避）"
	@echo "  make run-ex12a   - 課題1-2aを実行（ヒューリスティック探索）"
	@echo "  make run-ex12b   - 課題1-2bを実行（8パズル）"
	@echo "  make run-ex12c   - 課題1-2cを実行（8パズル比較）"
	@echo "  make run-ex12d   - 課題1-2dを実行（複数ヒューリスティック）"
	@echo ""
	@echo "クリーンアップ:"
	@echo "  make clean       - コンパイルされたファイルを削除"
	@echo "  make clean-all   - 全ての生成ファイルを削除"
	@echo ""
	@echo "その他:"
	@echo "  make test        - 主要課題をテスト実行"
	@echo "  make help        - このヘルプを表示"
	@echo "  make all         - デフォルト（compile と同じ）"
	@echo ""
	@echo "========================================"

# デフォルトでhelpを表示しない
.DEFAULT_GOAL := all
