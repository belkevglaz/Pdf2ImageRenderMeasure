#!/bin/bash

OLDIFS=$IFS
IFS=";"

# try to transpone csv to be able use in plot
# we have 6 print_arrays for each pages dimension that will keep data for it
# each print_arrays contains (ghost_75dpi pdfbox_75dpi ghost_150dpi pdfbox_150dpi ghost_300dpi pdfbox_300dpi)
# .........print.............([0]=0       [1]=0        [2]=0        [3]=0         [4]=0        [5]=0)
# .........raster............([6]=0       [7]=0        [8]=0        [9]=0         [10]=0       [11]=0)

function calculate_position() {
  local result=$1

  local dpi_delta=-100
  if [[ $dpi -eq 75 ]]; then
    dpi_delta=0
  elif [[ $dpi -eq 150 ]]; then
    dpi_delta=2
  elif [[ $dpi -eq 300 ]]; then
    dpi_delta=4
  fi

  local render_delta=-100
  if [[ "$render" = "ghostj4" ]]; then
    render_delta=0
  else
    render_delta=1
  fi

  local type_delta=-100
  if [[ "$type" = "print" ]]; then
    type_delta=0
  else
    type_delta=6
  fi
  eval $result=$(($dpi_delta + $render_delta + $type_delta))

}

pages_1_print_array=()
pages_2_print_array=()
pages_5_print_array=()
pages_10_print_array=()
pages_20_print_array=()
pages_30_print_array=()
while read type dpi render pages time; do
    if [[ $pages -eq 1  ]]; then
      calculate_position postion
      echo "Pages Array 1 position :" $postion
      pages_1_print_array[$postion]=$time
    fi
    if [[ $pages -eq 2  ]]; then
      calculate_position postion
      echo "Pages Array 2 position :" $postion
      pages_2_print_array[$postion]=$time
    fi
    if [[ $pages -eq 5  ]]; then
      calculate_position postion
      echo "Pages Array 5 position :" $postion
      pages_5_print_array[$postion]=$time
    fi
    if [[ $pages -eq 10  ]]; then
      calculate_position postion
      echo "Pages Array 10 position :" $postion
      pages_10_print_array[$postion]=$time
    fi
    if [[ $pages -eq 20  ]]; then
      calculate_position postion
      echo "Pages Array 20 position :" $postion
      pages_20_print_array[$postion]=$time
    fi
    if [[ $pages -eq 30  ]]; then
      calculate_position postion
      echo "Pages Array 30 position :" $postion
      pages_30_print_array[$postion]=$time
    fi
  done <$1

echo 1 ${pages_1_print_array[*]} > plot.dat
echo 2 ${pages_2_print_array[*]} >> plot.dat
echo 5 ${pages_5_print_array[*]} >> plot.dat
echo 10 ${pages_10_print_array[*]} >> plot.dat
echo 20 ${pages_20_print_array[*]} >> plot.dat
echo 30 ${pages_30_print_array[*]} >> plot.dat

IFS=$OLDIFS
