#!/bin/gnuplot
# set terminal pngcairo  font "arial,10" fontscale 1.0 size 1000, 800
# set output 'timings.png'
set bar 1.000000 front
set border 3 front lt black linewidth 1.000 dashtype solid
set boxwidth 1 absolute
set style fill   solid 1.00 noborder
set style circle radius graph 0.02, first 0.00000, 0.00000
set style ellipse size graph 0.05, 0.03, first 0.00000 angle 0 units xy
set grid nopolar

set grid noxtics nomxtics ytics nomytics noztics nomztics \
 nox2tics nomx2tics noy2tics nomy2tics nocbtics nomcbtics
set grid layerdefault   lt 0 linewidth 0.500,  lt 0 linewidth 0.500
set key bmargin center horizontal Left reverse noenhanced autotitle columnhead nobox
set style histogram clustered gap 1 title textcolor lt -1 offset character 2, -3, 40
set style textbox transparent margins  1.0,  1.0 border

set style data histograms
set xtics border in scale 0,0 nomirror rotate by -45  autojustify
set xtics  norangelimit  font ",8"

set title "Renders battle : Ghost4j VS PdfBox"
set ylabel "Perform time in millisecs"
set xlabel  offset character 0, -2, 0 font "" textcolor lt -1 norotate

set colorbox vertical origin screen 0.9, 0.2, 0 size screen 0.05, 0.6, 0 front  noinvert bdefault
x = 0.0
i = 24

plot newhistogram "Print 75", 'plot.dat' using "PGH075":xtic(1) t "Ghost4j", '' using "PPB075":xtic(1) t "PdfBox", \
	newhistogram "Print 150", '' using "PGH150":xtic(1) t "Ghost4j", '' using "PPB150":xtic(1) t "PdfBox", \
	newhistogram "Print 300", '' using "PGH300":xtic(1) t "Ghost4j", '' using "PPB300":xtic(1) t "PdfBox", \
	newhistogram "Raster 75", '' using "PGH075":xtic(1) t "Ghost4j", '' using "PPB075":xtic(1) t "PdfBox", \
	newhistogram "Raster 150", '' using "PGH150":xtic(1) t "Ghost4j", '' using "PPB150":xtic(1) t "PdfBox", \
	newhistogram "Raster 300", '' using "PGH300":xtic(1) t "Ghost4j", '' using "PPB300":xtic(1) t "PdfBox"