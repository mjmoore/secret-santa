# Yet Another Secret Santa Supplier! (YASSS!)

## YASSS! - But why?

This year we've decided to have two `Secret Santa`s each, which doesn't seem to be a common feature for many `Secret Santa` generators, so here's yet another one.


## Configuration

An `application.yml` is required to create secret Santas

An `application-sample.yml` is provided in the [resources](./src/main/resources/application-sample.yml) directory. Copy it to `src/main/resources/application.yml` and fill it out as needed.

---

Exclusions are names of people who should not be `Secret Santa` for another person:

```
  - name: Queen
  - name: King
    exclusions:
      - Queen
  - name: Prince
    exclusions:
      - Queen
```

In the above example, `Queen` will not be `Secret Santa` for either `King` or `Prince`.

---

## Limitations

 * Names are case sensitive and must be an exact match.

 * No validation is done on configuration. Feel free to provide unsolvable configurations. They will not complete.
