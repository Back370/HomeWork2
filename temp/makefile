# LaTeX Makefile for Japanese documents

# ターゲットファイル（拡張子なし）
TARGET = 36714137

NUMBER = 14

# コンパイラ
LATEX = uplatex
BIBTEX = pbibtex
DVIPDF = dvipdfmx

# オプション
LATEX_OPT = -halt-on-error -interaction=nonstopmode
DVIPDF_OPT =

# ソースファイル
TEX_SRC = $(TARGET).tex
DVI_FILE = $(TARGET).dvi
PDF_FILE = $(TARGET).pdf

# 画像ファイル
IMAGE_DIR = images
PNG_FILES = $(wildcard $(IMAGE_DIR)/*.png)
EPS_FILES = $(PNG_FILES:.png=.eps)

# 中間ファイル
AUX_FILES = $(TARGET).aux $(TARGET).log $(TARGET).toc $(TARGET).lof $(TARGET).lot $(TARGET).out $(TARGET).bbl $(TARGET).blg

.PHONY: all pdf clean cleanall open clean-images process-images

# デフォルトターゲット
all: pdf

# PDFを生成
pdf: process-images $(PDF_FILE)

# 画像をクリーンアップしてEPSに変換
process-images: $(EPS_FILES)

$(IMAGE_DIR)/%.eps: $(IMAGE_DIR)/%.png
	@echo "Processing $<..."
	@magick $< -strip $(IMAGE_DIR)/$*_temp.png
	@mv $(IMAGE_DIR)/$*_temp.png $<
	@magick $< $@
	@echo "Generated $@"

# LaTeX -> DVI -> PDF
$(PDF_FILE): $(DVI_FILE)
	$(DVIPDF) $(DVIPDF_OPT) $<

$(DVI_FILE): $(TEX_SRC)
	$(LATEX) $(LATEX_OPT) $(TEX_SRC)
	@if grep -q '\\citation' $(TARGET).aux 2>/dev/null; then \
		$(BIBTEX) $(TARGET); \
		$(LATEX) $(LATEX_OPT) $(TEX_SRC); \
	fi
	$(LATEX) $(LATEX_OPT) $(TEX_SRC)

# PDFを開く
open: $(PDF_FILE)
	open $(PDF_FILE)

# 中間ファイルを削除
clean:
	rm -f $(AUX_FILES) $(DVI_FILE)

# 生成された画像ファイルを削除
clean-images:
	rm -f $(IMAGE_DIR)/*.eps $(IMAGE_DIR)/*.xbb $(IMAGE_DIR)/*.jpg

# 全てのファイルを削除（PDFも含む）
cleanall: clean clean-images
	rm -f $(PDF_FILE)

zip: $(PDF_FILE)
	cp $(PDF_FILE) $(TARGET)/$(TARGET)-$(NUMBER).pdf
	tar czvf $(TARGET)-$(NUMBER).tar.gz $(TARGET)

# ヘルプ
help:
	@echo "利用可能なターゲット:"
	@echo "  make              - PDFを生成（デフォルト）"
	@echo "  make pdf          - PDFを生成"
	@echo "  make process-images - 画像をクリーンアップしてEPSに変換"
	@echo "  make open         - PDFを生成して開く"
	@echo "  make clean        - 中間ファイルを削除"
	@echo "  make clean-images - 生成された画像ファイルを削除"
	@echo "  make cleanall     - 全てのファイルを削除（PDFと画像も含む）"
	@echo "  make zip          - ファイルを圧縮"
