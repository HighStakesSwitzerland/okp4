<!-- generated file - do not edit -->
# 🔗 `{{ (datasource "genesis").chain_id }}`

![chain-id](https://img.shields.io/badge/chain%20id-{{ (datasource "genesis").chain_id | urlquery | strings.ReplaceAll "-" "--" }}-blue?style=for-the-badge)
[![stability-deprecated](https://img.shields.io/badge/stability-deprecated-922b21.svg?style=for-the-badge)](https://github.com/mkenney/software-guides/blob/master/STABILITY-BADGES.md#deprecated)
![audience](https://img.shields.io/badge/audience-public-white.svg?style=for-the-badge)
![incentivized-✖️](https://img.shields.io/badge/incentivized-✖️-29220A.svg?style=for-the-badge)
![genesis-time](https://img.shields.io/badge/{{ "⏰" | urlquery }}%20genesis%20time-{{ (datasource "genesis").genesis_time | urlquery | strings.ReplaceAll "-" "--" }}-red?style=for-the-badge)
![chain-height](https://img.shields.io/badge/dynamic/json?url=https%3A%2F%2Fapi.drunemeton.okp4.network%2Fcosmos%2Fbase%2Ftendermint%2Fv1beta1%2Fblocks%2Flatest&query=%24.block.header.height&style=for-the-badge&label=height&color=%231E2A4E&cacheSeconds=6)

> [!IMPORTANT]
> This network was discontinued on 2024-09-30.
<!-- -->
> [!IMPORTANT]
> This network was originally created under the former `OKP4` brand.

## Register in the Genesis

To register your validator node in the `genesis.json` you just need to provide a signed `gentx` with an initial delegation of `10000000000uknow` in a [⚖️ Register Validator issue](https://github.com/okp4/networks/issues).

The gentx generation can be done as follow (this is an example script, adapt it to your needs) with the [okp4d](https://github.com/okp4/okp4d/releases) binary matching the [network's version](/chains/drunemeton-1/version.txt):

```sh
# Init node
okp4d --home mynode init your-moniker --chain-id {{ (datasource "genesis").chain_id }}

# Create keys, be careful with the mnemonic 👀
okp4d --home mynode keys add your-key-name

# Set account necessary balance
okp4d --home mynode genesis add-genesis-account your-key-name 10000200000uknow
```

Then create your own genesis transaction (`gentx`). You will have to choose the following parameters for your validator: `commission-rate`, `commission-max-rate`, `commission-max-change-rate`, `min-self-delegation` (>=1), `website` (optional), `details` (optional), `identity` ([keybase](https://keybase.io) key hash, used to get validator logos in block explorers - optional), `security-contact` (email - optional).

```sh
# Create the gentx
okp4d --home mynode genesis gentx your-key-name 10000000000uknow \
  --node-id $(okp4d --home mynode tendermint show-node-id) \
  --chain-id okp4-drunemeton-1 \
  --commission-rate 0.05 \
  --commission-max-rate 0.2 \
  --commission-max-change-rate 0.01 \
  --min-self-delegation 1 \
  --website "https://foo.network" \
  --details "My validator" \
  --identity "6C36E7C076BFDCE4" \
  --security-contact "validator@foo.network"
```

## Genesis validators

<table>
  <tr>
    <th>Moniker</th>
    <th>Details</th>
    <th>Identity</th>
    <th>Site</th>
  </tr>
{{- $txs := (datasource "genesis") | jsonpath "$..messages[?(@.min_self_delegation)]" -}}
{{- range $key, $value := $txs }}
{{- $url := "" -}}
{{- if $value.description.website | strings.HasPrefix "http" -}}
{{- $url = $value.description.website -}}
{{- else if $value.description.website -}}
{{- $url = printf "%s%s" "https://" $value.description.website -}}
{{- end -}}
{{- $userInfo := $value.description.identity | index (datasource "usersInfo") }}
  <tr>
    <td><pre>{{ $value.description.moniker | html }}</pre></td>
    <td>{{ $value.description.details | html }}</td>
    <td>{{ if (and $value.description.identity $userInfo) }}
      <p align="center"><img width="80px" src="{{ $userInfo.keybase.picture_url }}"/></p>
      <a href="https://keybase.io/{{ $userInfo.keybase.username }}">{{ $value.description.identity }}</a>{{ end }}</td>
    <td>{{ if $url }}<a href="{{ $url }}">{{ $url }}</a>{{ end -}}
  </tr>
{{- end }}
</table>
