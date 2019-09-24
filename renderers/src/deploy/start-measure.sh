#!/bin/bash

# we neen to run several tests to both renderers for each file
echo "The script you are running has basename `basename "$0"`, dirname `dirname "$0"`"
echo "The present working directory is `pwd`"
echo "The absolute path : $(dirname $(readlink -f $0))"
echo ""

TARGET=$(dirname $(readlink -f $0))
cd $TARGET
DIR=$TARGET/files

if [ -d "$DIR" ]; then
  ### Take action if $DIR exists ###
  echo "Pdf printable files in ${DIR}"
else
  echo "No files directory. Run manually"
  exit 1
fi

OUTPUT=result-timing-$(date "+%Y%m%d%H%M%S").csv
for dpi in 75 150 300 ; do
    TYPE=print
    for file in ./files/performance/$TYPE/*.pdf
    do
      echo "Start perform [$TYPE] pdfs with dpi [$dpi] : $file"
      java -jar render-measure.jar -d $dpi -r ghost4j -f $file -o $OUTPUT  -t $TYPE
      java -jar render-measure.jar -d $dpi -r pdfbox -f $file -o $OUTPUT  -t $TYPE
    done

    TYPE=raster
    for file in ./files/performance/$TYPE/*.pdf
    do
      echo "Start perform [$TYPE] pdfs with dpi [$dpi] : $file"
      java -jar render-measure.jar -d $dpi -r ghost4j -f $file -o $OUTPUT  -t $TYPE
      java -jar render-measure.jar -d $dpi -r pdfbox -f $file -o $OUTPUT -t $TYPE
    done
done

sh ./transponer.sh < $OUTPUT
sh ./timing.gp -p

