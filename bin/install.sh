#!/bin/bash
cur_dir=$(dirname "$(readlink -f "$0")") && dos2unix ${cur_dir}/*.sh && echo "cur_dir : ${cur_dir}"
echo "cur_dir : ${cur_dir}"
# 项目基本目录
base_dir=$(dirname "${cur_dir}")
echo "base_dir : ${base_dir}"
# 项目名，建议配置为 maven 的默认jar命名风格
tar_name=$(basename "${base_dir}")
project_name=${tar_name%-*}
echo "project_name : ${project_name}"
#rm -rf /usr/local/"${project_name:?}"*
rm -rf /tmp/backup/${project_name}
mkdir -p /tmp/backup/${project_name}
mv -f /usr/local/${project_name}* /tmp/backup/${project_name}
echo "backup path : /tmp/backup/${project_name}"
mkdir -p /usr/local/${tar_name}
cp -rf ${base_dir} /usr/local/
sh -c /usr/local/${tar_name}/bin/start.sh
