#!/bin/bash
cur_dir=$(dirname "$(readlink -f "$0")") && dos2unix ${cur_dir}/*.sh && echo "cur_dir : ${cur_dir}"
# 项目基本目录
base_dir=$(dirname "${cur_dir}")
echo "base_dir : ${base_dir}"
# 项目名，建议配置为 maven 的默认jar命名风格
app_full_name=$(basename "${base_dir}")
app_name=${app_full_name%-*}
echo "app_name : ${app_name}"
# 检查程序是否已启动，如果已启动，则结束它
check_pid=$(ps -ef | grep java | grep "${app_name}" | grep -v grep | awk '{print $2}')
if [ x"${check_pid}" != x"" ]; then
  echo "${app_name} is running.pid is ${check_pid}. try kill it"
  kill -15 "${check_pid}"
  sleep 3s
  kill -9 "${check_pid}"
  echo "stop is ok"
fi
