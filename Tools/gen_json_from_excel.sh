#!/bin/zsh
WORKSPACE=./Luban.ClientServer

GEN_CLIENT=${WORKSPACE}/Luban.ClientServer.dll
CONF_ROOT=../meta
EXCEL_PATH=./Datas

dotnet ${GEN_CLIENT} -j cfg --\
  -d Defines/__root__.xml \
  --input_data_dir ${EXCEL_PATH} \
  --output_data_dir output_json \
  --output_code_dir ${EXCEL_PATH}/src/main/java/com/a3fun/pudding/conf/ \
  -s all \
  --gen_types data_json2,code_java_json


 mv output_json/* ${CONF_ROOT}/
