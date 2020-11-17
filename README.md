[![Build Status](https://travis-ci.org/molgenis/vkgl-vcf-converter.svg?branch=master)](https://travis-ci.org/molgenis/vkgl-vcf-converter)
[![Quality Status](https://sonarcloud.io/api/project_badges/measure?project=molgenis_vkgl-vcf-converter&metric=alert_status)](https://sonarcloud.io/dashboard?id=molgenis_vkgl-vcf-converter)
# VKGL consensus to VCF converter
Command-line application to convert a [VKGL consensus .csv file](https://github.com/molgenis/molgenis-py-consensus) to a VCF file.

## Requirements
- Java 11

## Usage
```
usage: java -jar vkgl-vcf-converter.jar -i <arg> [-o <arg>] [-f] [-d]
 -i,--input <arg>    Input VKGL consensus file (.csv).
 -o,--output <arg>   Output VCF file (.vcf or .vcf.gz).
 -p,--public         Write VCF file containing public information only.
 -f,--force          Override the output file if it already exists.
 -d,--debug          Enable debug mode (additional logging).

usage: java -jar vkgl-vcf-converter.jar -v
 -v,--version   Print version.
```

## Examples
```
java -jar vkgl-vcf-converter.jar -i vkgl_consensus.csv -o vkgl_consensus.vcf
java -jar vkgl-vcf-converter.jar -v
```