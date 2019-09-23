set datafile separator ";"
set title 'Renderer performance'
set xlabel 'pages count'
set ylabel 'perform time / ms'

set grid
set key below center horizontal noreverse enhanced autotitle box dashtype solid
set tics out nomirror
set border 3 front linetype black linewidth 1.0 dashtype solid

set xrange [-1:3]
set xtics 1
#set mxtics 1

set yrange [0:80]
# set ytics 5

set style line 1 linecolor rgb '#0060ad' linetype 1 linewidth 2

set style histogram clustered gap 1 title offset character 0, 0, 0
set style data histograms

set boxwidth 1.0 absolute
set style fill solid 5.0 border -1

plot "result-timing-75-20190923191501.csv" using 4:5