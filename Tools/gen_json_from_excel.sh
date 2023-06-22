#!/bin/zsh
WORKSPACE=./Luban.ClientServer

GEN_CLIENT=${WORKSPACE}/Luban.ClientServer.dll
CONF_ROOT=../meta
EXCEL_PATH=./Datas

dotnet ${GEN_CLIENT} -j cfg --\
  -d Defines/__root__.xml \
  --input_data_dir ${EXCEL_PATH} \
  --output_data_dir output_json \
  -s all \
  --gen_types data_json2