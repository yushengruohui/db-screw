#!/bin/bash
cur_dir=$(dirname "$(readlink -f "$0")") && dos2unix ${cur_dir}/*.sh && echo "cur_dir : ${cur_dir}"
# 项目基本目录
base_dir=$(dirname "${cur_dir}")
echo "base_dir : ${base_dir}"
# 项目名，建议配置为 maven 的默认jar命名风格
app_full_name=$(basename "${base_dir}")
app_name=${app_full_name%-*}
echo "app_name : ${app_name}"
sh -c /usr/local/"${app_full_name}"/bin/stop.sh
rm -rf /usr/local/"${app_name:?}"*
echo "uninstall is ok"
