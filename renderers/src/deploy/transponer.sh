#!/bin/bash

OLDIFS=$IFS
IFS=";"
########################################################################################################################
#
# try to transpone csv to be able use in plot
# we have 6 print_arrays for each pages dimension that will keep data for it
# each print_arrays contains (ghost_75dpi pdfbox_75dpi ghost_150dpi pdfbox_150dpi ghost_300dpi pdfbox_300dpi)
# .........print.............([0]=0       [1]=0        [2]=0        [3]=0         [4]=0        [5]=0)
# .........raster............([6]=0       [7]=0        [8]=0        [9]=0         [10]=0       [11]=0)
#
########################################################################################################################

HEAD_PRINT='PGH075 PPB075 PGH150 PPB150 PGH300 PPB300'
HEAD_RASTER='RGH075 RPB075 RGH150 RPB150 RGH300 RPB300'

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
pages_15_print_array=()
pages_30_print_array=()
pages_60_print_array=()
ROW=0
while read type dpi render pages time || [ -n "$type" ]; do
    if [[ $pages -eq 1  ]]; then
      calculate_position postion
      pages_1_print_array[$postion]=$time
    fi
    if [[ $pages -eq 2  ]]; then
      calculate_position postion
      pages_2_print_array[$postion]=$time
    fi
    if [[ $pages -eq 5  ]]; then
      calculate_position postion
      pages_5_print_array[$postion]=$time
    fi
    if [[ $pages -eq 15  ]]; then
      calculate_position postion
      pages_15_print_array[$postion]=$time
    fi
    if [[ $pages -eq 30  ]]; then
      calculate_position postion
      pages_30_print_array[$postion]=$time
    fi
    if [[ $pages -eq 60  ]]; then
      calculate_position postion
      pages_60_print_array[$postion]=$time
    fi
    ROW=$((ROW + 1))
    echo -e "[$ROW]\t[$type]\t[$dpi]\t[render]\t[pages] == > "$postion"\t[$time]"
  done <$1

echo "PAGES" $HEAD_PRINT $HEAD_RASTER > plot2.dat

echo 1 ${pages_1_print_array[*]} >> plot2.dat
echo 2 ${pages_2_print_array[*]} >> plot2.dat
echo 5 ${pages_5_print_array[*]} >> plot2.dat
echo 15 ${pages_15_print_array[*]} >> plot2.dat
echo 30 ${pages_30_print_array[*]} >> plot2.dat
echo 60 ${pages_60_print_array[*]} >> plot2.dat

tr ' ' \\t < plot2.dat > plot.dat
cat plot.dat

IFS=$OLDIFS
