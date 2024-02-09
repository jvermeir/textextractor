BIN=$(cd "$(dirname "$0")"; pwd -P)
echo linking to ${BIN}/run.sh
rm -f /usr/local/bin/torconvert
ln -s ${BIN}/run.sh /usr/local/bin/torconvert
